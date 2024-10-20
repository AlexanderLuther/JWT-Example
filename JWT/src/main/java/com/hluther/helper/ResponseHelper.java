package com.hluther.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

public final class ResponseHelper {

    private static final String APPLICATION_JSON = "application/json";

    public static void setResponse(HttpServletResponse response, Object entity, int statusCode) {
        response.setContentType(APPLICATION_JSON);
        response.setStatus(statusCode);
        try {
            if (entity != null) {
                response.getOutputStream().write(
                        new ObjectMapper().writeValueAsString(entity).getBytes()
                );
                response.getOutputStream().flush();
            }
        }  catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
