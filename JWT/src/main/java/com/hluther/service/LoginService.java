package com.hluther.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hluther.entity.exception.ApiException;
import com.hluther.entity.User;
import com.hluther.helper.JWTHelper;
import jakarta.servlet.http.HttpServletResponse;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class LoginService {

    public String login(String requestBody) throws JsonProcessingException, ApiException{
        if (isEmpty(requestBody) || isBlank(requestBody)) {
            throw new ApiException(
                    "User is required.",
                    HttpServletResponse.SC_BAD_REQUEST
            );
        }

        User user = new ObjectMapper().readValue(requestBody, User.class);

        if(isEmpty(user.getName()) || isBlank(user.getName())){
            throw new ApiException(
                    "Name is required.",
                    HttpServletResponse.SC_BAD_REQUEST
            );
        }

        if(isEmpty(user.getUsername()) || isBlank(user.getUsername())){
            throw new ApiException(
                    "Username is required.",
                    HttpServletResponse.SC_BAD_REQUEST
            );
        }

        if(isEmpty(user.getRol()) || isBlank(user.getRol())){
            throw new ApiException(
                    "Rol is required.",
                    HttpServletResponse.SC_BAD_REQUEST
            );
        }

        return JWTHelper.getToken(user);
    }
}
