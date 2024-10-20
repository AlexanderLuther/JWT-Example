package com.hluther.controller;

import com.hluther.entity.Message;
import com.hluther.entity.User;
import com.hluther.entity.exception.ApiException;
import com.hluther.helper.ResponseHelper;
import com.hluther.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "UserController", urlPatterns = {"/api/user"})
public class UserController extends HttpServlet {

    private final UserService userService;

    public UserController(){
        this.userService = new UserService();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = userService.getUserInfo(req.getHeader("Authorization"));
            ResponseHelper.setResponse(
                    resp,
                    user,
                    HttpServletResponse.SC_OK
            );
        } catch (ApiException e) {
            ResponseHelper.setResponse(
                    resp,
                    Message.builder().message(e.getMessage()).build(),
                    e.getStatusCode()
            );
        } catch (Exception e) {
            ResponseHelper.setResponse(
                    resp,
                    Message.builder().message("An error occurred.").build(),
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
        }
    }
}
