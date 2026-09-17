package com.manuel.taskmanager.controller;

import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

public class uthController {

    @GetMapping("/whoami")
    public String whoami(
            HttpServletRequest request) {

        return request.getHeader("Authorization");
    }

}
