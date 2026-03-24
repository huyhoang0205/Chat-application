package com.huyhoang25.chatapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huyhoang25.chatapp.dto.request.CreateUserRequest;
import com.huyhoang25.chatapp.dto.response.ApiResponse;
import com.huyhoang25.chatapp.dto.response.CreateUserResponse;
import com.huyhoang25.chatapp.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public ApiResponse<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        var data = userService.createUser(request);

        return ApiResponse.<CreateUserResponse>builder()
        .code(HttpStatus.OK.value())
        .message("User created Succesfully")
        .data(data)
        .build();
    }
}

