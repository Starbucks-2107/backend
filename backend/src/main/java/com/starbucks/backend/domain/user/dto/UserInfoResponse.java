package com.starbucks.backend.domain.user.dto;

public record UserInfoResponse(
        Long userId,
        String email,
        String username,
        String nickname,
        String birthday,
        String phoneNumber) {}
