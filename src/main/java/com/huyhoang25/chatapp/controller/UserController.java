package com.huyhoang25.chatapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huyhoang25.chatapp.dto.request.CreateUserRequest;
import com.huyhoang25.chatapp.dto.response.ApiResponse;
import com.huyhoang25.chatapp.dto.response.CreateUserResponse;
import com.huyhoang25.chatapp.dto.response.PageResponse;
import com.huyhoang25.chatapp.dto.response.UserDetailResponse;
import com.huyhoang25.chatapp.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ApiResponse<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        var data = userService.createUser(request);

        return ApiResponse.<CreateUserResponse>builder()
        .code(HttpStatus.OK.value())
        .message("User created Succesfully")
        .data(data)
        .build();
    }

    @GetMapping
    public ApiResponse<UserDetailResponse> myInfo(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();

        var data = userService.myInfo(userId);

        return ApiResponse.<UserDetailResponse>builder()
        .code(HttpStatus.OK.value())
        .message("User info retrieved successfully!")
        .data(data)
        .build();
    }

    @GetMapping("/search")
    public ApiResponse<PageResponse<UserDetailResponse>> searchUser(
        @RequestParam(required = false, defaultValue = "1") int page,
        @RequestParam(required = false, defaultValue = "5") int size,
        @RequestParam(required = false) String keyword
    ) {
        var data = userService.searchUsers(keyword, page, size);
        return ApiResponse.<PageResponse<UserDetailResponse>>builder()
        .code(HttpStatus.OK.value())
        .message("User retrieved successfully!")
        .data(data)
        .build();
    }
} 

