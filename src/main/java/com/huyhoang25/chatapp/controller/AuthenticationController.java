package com.huyhoang25.chatapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huyhoang25.chatapp.dto.request.LoginRequest;
import com.huyhoang25.chatapp.dto.response.ApiResponse;
import com.huyhoang25.chatapp.dto.response.LoginResponse;
import com.huyhoang25.chatapp.service.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final  AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        var data = authenticationService.Login(request);
        return ApiResponse.<LoginResponse>builder()
        .code(HttpStatus.OK.value())
        .message("Login Successfully!")
        .data(data)
        .build();
    }
}
