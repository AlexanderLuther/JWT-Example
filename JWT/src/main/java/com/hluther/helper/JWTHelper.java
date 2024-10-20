package com.hluther.helper;

import com.hluther.entity.User;
import com.hluther.entity.exception.ApiException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import static jakarta.servlet.http.HttpServletResponse.SC_BAD_GATEWAY;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class JWTHelper {

    private static final String SECRET = "super-secret-key-must-be-256-bits-long";
    private static final int EXPIRATION_TIME_IN_DAYS = 1;

    /**
     * Generates a signed JWT token.
     * @param user User object.
     * @return Signed JWT token.
     * @throws ApiException If unable to sign JWT.
     */
    public static String getToken(User user) throws ApiException{
        return JWTHelper.getSignedToken(
                JWTHelper.getHeader(),
                JWTHelper.getClaims(user.getUsername(), user.getName(), user.getRol())
        ).serialize();
    }

    /**
     * Verifies a JWT token.
     * @param token String JWT token.
     * @return User object.
     * @throws ApiException If unable to parse token or verify token.
     */
    public static User verifyToken(String token) throws ApiException{
        try {
            token = JWTHelper.removeBearer(token);
            JWSObject jwsObject = JWSObject.parse(token);
            JWSVerifier verifier = new MACVerifier(SECRET.getBytes());
            if (jwsObject.verify(verifier)) {
                SignedJWT signedJWT = SignedJWT.parse(jwsObject.getParsedString());
                JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
                JWTHelper.verifyExpirationTime(claims);
                return new User(
                        claims.getClaim("username").toString(),
                        claims.getClaim("name").toString(),
                        claims.getClaim("rol").toString()
                );
            }
        } catch (ParseException | JOSEException e ) {
            System.out.println("Error verifying JWT: " + e.getMessage());
        }
        throw new ApiException("Unauthorized.", SC_UNAUTHORIZED);
    }

    /********************************************************
     ************    METHODS FOR GENERATE JWT   ************
     *******************************************************/
    private static JWSHeader getHeader() {
        return new JWSHeader.Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT)
                .build();
    }

    private static JWTClaimsSet getClaims(String username, String name, String rol) {
        Date currentDate = new Date();
        return new JWTClaimsSet.Builder()
                //.subject("some subject")
                //.audience("some audience")
                //.issuer("https://yourdomain.com")
                //.jwtID("some id") // jti
                .issueTime(currentDate) // iat
                .notBeforeTime(currentDate) // nbf
                .expirationTime(DateUtils.addDays(currentDate, EXPIRATION_TIME_IN_DAYS)) // exp
                .claim("username", username)
                .claim("name", name)
                .claim("rol", rol)
                .build();
    }

    private static SignedJWT getSignedToken(JWSHeader header, JWTClaimsSet claims) throws ApiException {
        try {
            SignedJWT signedJWT = new SignedJWT(header, claims);
            signedJWT.sign(new MACSigner(SECRET.getBytes()));
            return signedJWT;
        } catch (JOSEException e) {
            System.out.println("Error creating JWT: " + e.getMessage());
            throw new ApiException("Error generating token.", SC_BAD_GATEWAY);
        }
    }

    /********************************************************
     ************    METHODS FOR VERIFY JWT   ************
     *******************************************************/
    private static void verifyExpirationTime(JWTClaimsSet claims) throws ApiException {
        long expirationTime = claims.getExpirationTime().getTime();
        if (expirationTime < System.currentTimeMillis()) {
            throw new ApiException("Token expired.", SC_UNAUTHORIZED);
        }
    }

    private static String removeBearer(String token) throws ApiException{
        if(isEmpty(token) || isBlank(token)) {
            throw new ApiException("Unauthorized.", SC_UNAUTHORIZED);
        }
        return token.replace("Bearer ", "");
    }
}
