package com.huyhoang25.chatapp.service;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.huyhoang25.chatapp.exception.AppException;
import com.huyhoang25.chatapp.exception.ErrorCode;
import com.huyhoang25.chatapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomDetailUserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String username) {
        return userRepository.findByEmail(username)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}
