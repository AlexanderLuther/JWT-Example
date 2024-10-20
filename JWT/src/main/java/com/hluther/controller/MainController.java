package com.hluther.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hluther.entity.Message;
import com.hluther.helper.ResponseHelper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@WebServlet(name = "MainController", urlPatterns = {"/"})
public class MainController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ResponseHelper.setResponse(
                resp,
                new ObjectMapper().writeValueAsString(
                        Message.builder()
                                .message("API INICIADA")
                                .build()
                ),
                SC_OK
        );
    }
}
