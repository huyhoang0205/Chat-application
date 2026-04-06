package com.huyhoang25.chatapp.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.huyhoang25.chatapp.dto.request.LoginRequest;
import com.huyhoang25.chatapp.dto.response.LoginResponse;
import com.huyhoang25.chatapp.dto.response.StatusResponse;
import com.huyhoang25.chatapp.entity.User;
import com.huyhoang25.chatapp.exception.AppException;
import com.huyhoang25.chatapp.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
//wf: user,pw -> UsernamePasswordAuthenticationToken(user.pw) -> 
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserSessionService userSessionService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public LoginResponse Login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        User user = (User) authentication.getPrincipal();

        if(user == null) throw new AppException(ErrorCode.UNAUTHORIZED);

        Set<String> authories = user.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toSet());

        String accessToken = jwtService.generateAccessToken(user.getId(), authories);
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        Boolean status = userSessionService.isOnline(user.getId());
        
        if(!status) {
            StatusResponse response = StatusResponse.builder()
                                            .userId(user.getId())
                                            .isOnline(!status)
                                            .lastOnlineAt("")
                                            .build();
            simpMessagingTemplate.convertAndSend("/topic/status", response);
        }

        return LoginResponse.builder()
        .userId(user.getId())
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
    }

    
}
