package org.starbucks.backend.global.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/auth/token")
@RequiredArgsConstructor
public class JWTController {
    private final JWTService jwtService;

    @PostMapping(value = "/refresh")
    public void refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        jwtService.refreshAccessToken(request, response);
    }
}
