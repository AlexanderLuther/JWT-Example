package com.hluther.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hluther.entity.exception.ApiException;
import com.hluther.entity.Message;
import com.hluther.helper.ResponseHelper;
import com.hluther.service.LoginService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet(name = "LoginController", urlPatterns = {"/api/login"})
public class LoginController extends HttpServlet {

    private final LoginService loginService;

    public LoginController() {
        this.loginService = new LoginService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String token = loginService.login(req.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
            ResponseHelper.setResponse(
                    resp,
                    Message.builder().message(token).build(),
                    HttpServletResponse.SC_OK
            );
        } catch (JsonProcessingException e) {
            ResponseHelper.setResponse(
                    resp,
                    Message.builder().message("Unable to process provided data.").build(),
                    HttpServletResponse.SC_BAD_REQUEST
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


