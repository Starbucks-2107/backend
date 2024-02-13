package com.starbucks.backend.global.jwt.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    public void create(String refreshToken, HttpServletResponse response) {
        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .maxAge(Integer.MAX_VALUE)
                .build();

        response.addHeader("Set-Cookie", responseCookie.toString());
    }

    public void delete(String refreshToken, HttpServletResponse response) {
        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", responseCookie.toString());
    }

    public Cookie getRefreshTokenFromCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) return cookie;
        }
        return null;
    }
}
