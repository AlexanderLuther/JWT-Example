package com.hluther.entity.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApiException extends Exception {
    String message;
    Integer statusCode;

    public ApiException(String message, Integer statusCode){
        this.message = message;
        this.statusCode = statusCode;
    }
}
