package com.starbucks.backend.global.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        if (authException instanceof UsernameNotFoundException) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            setResponse(response, "해당 유저를 찾을 수 없습니다.", "AUTH_02");

        } else if (authException instanceof BadCredentialsException) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            setResponse(response, "잘못된 비밀먼호를 입력하였습니다.", "AUTH_03");

        } else if (authException instanceof InsufficientAuthenticationException) {
            // TODO: 이렇게 안뜨면 삭제 하기
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            setResponse(response, "accessToken 이 만료 되었습니다.", "TOKEN_01");

        } else if (authException.getCause() instanceof ExpiredJwtException) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            setResponse(response, "accessToken 이 만료 되었습니다.", "TOKEN_01");

        } else if (authException.getCause() instanceof MalformedJwtException) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            setResponse(response, "Token 의 형식을 다시 한번 확인 해주세요.", "TOKEN_02");
        }
    }

    private void setResponse(HttpServletResponse response, String message, String errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(
                "{\"message\" : \"" +  message + "\"," +
                        "\"errorCode\" : \"" + errorCode +
                        "\"}");
    }
}

