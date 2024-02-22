package org.starbucks.backend.global.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.starbucks.backend.global.jwt.util.CookieUtil;
import org.starbucks.backend.global.jwt.util.RedisUtil;
import org.starbucks.backend.global.jwt.util.TokenUtil;

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
        String employeeId = tokenUtil.getEmployeeIdFromToken(refreshToken).toString();

        log.info("======================================================");
        log.info("refreshToken: " + refreshToken);
        log.info("employeeId: " + employeeId);
        log.info("======================================================");

        String redisValue = redisUtil.getData(employeeId);
        if (refreshToken.equals(redisValue)) {
            String newAccessToken = tokenUtil.generateAccessToken(employeeId);
            String newRefreshToken = tokenUtil.generateRefreshToken(employeeId);

            response.setHeader("Authorization", newAccessToken);
            cookieUtil.create(newRefreshToken, response);
        }
    }
}
