package com.huyhoang25.chatapp.configuration;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.huyhoang25.chatapp.exception.ErrorCode;
import com.huyhoang25.chatapp.exception.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{


    @Override
    public void commence(HttpServletRequest request, 
                        HttpServletResponse response,
                        @NonNull AuthenticationException authException
    ) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        ErrorResponse errorResponse = ErrorResponse.builder()
        .code(errorCode.getCode())
        .status(errorCode.getHttpStatus().value())
        .error(errorCode.getHttpStatus().getReasonPhrase())
        .message(errorCode.getMessage())
        .path(request.getRequestURI())
        .build();

        JsonMapper obJsonMapper = new JsonMapper();
        response.getWriter().write(obJsonMapper.writeValueAsString(errorResponse));
        response.flushBuffer();
    }
}
