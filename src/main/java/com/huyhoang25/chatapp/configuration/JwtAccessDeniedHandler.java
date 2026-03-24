package com.huyhoang25.chatapp.configuration;

import java.io.IOException;

import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.huyhoang25.chatapp.exception.ErrorCode;
import com.huyhoang25.chatapp.exception.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler{

    public void handle(@NonNull HttpServletRequest request,
        HttpServletResponse response,
        @NonNull AccessDeniedException accessDeniedException
     ) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorCode errorCode = ErrorCode.FORBIDDEN;
        ErrorResponse errorResponse = ErrorResponse.builder()
        .code(errorCode.getCode())
        .status(errorCode.getHttpStatus().value())
        .error(errorCode.getHttpStatus().getReasonPhrase())
        .message(errorCode.getMessage())
        .path(request.getRequestURI())
        .build();

        JsonMapper jsonMapper = new JsonMapper();

        response.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
        response.flushBuffer();
     }

}
