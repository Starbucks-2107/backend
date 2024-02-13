package com.starbucks.backend.global.jwt;

import com.starbucks.backend.global.jwt.util.CookieUtil;
import com.starbucks.backend.global.jwt.util.RedisUtil;
import com.starbucks.backend.global.jwt.util.TokenUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JWTService {
    private final TokenUtil tokenUtil;
    private final RedisUtil redisUtil;
    private final CookieUtil cookieUtil;

    public void refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie refreshTokenCookie = cookieUtil.getRefreshTokenFromCookie(request, "refreshToken");

        String refreshToken = refreshTokenCookie.getValue();
        String userId = tokenUtil.getUserIdFromToken(refreshToken).toString();

        log.info("======================================================");
        log.info("refreshToken: " + refreshToken);
        log.info("userId: " + userId);
        log.info("======================================================");

        String redisValue = redisUtil.getData(userId);
        if (refreshToken.equals(redisValue)) {
            String newAccessToken = tokenUtil.generateAccessToken(userId);
            String newRefreshToken = tokenUtil.generateRefreshToken(userId);

            response.setHeader("Authorization", newAccessToken);
            cookieUtil.create(newRefreshToken, response);
        }
    }
}
