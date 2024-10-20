package com.hluther.service;

import com.hluther.entity.User;
import com.hluther.entity.exception.ApiException;
import com.hluther.helper.JWTHelper;

public class UserService {

    public User getUserInfo(String token) throws ApiException {
        return JWTHelper.verifyToken(token);
    }
}
