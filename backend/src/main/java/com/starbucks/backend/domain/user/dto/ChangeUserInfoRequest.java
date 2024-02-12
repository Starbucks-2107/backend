package com.starbucks.backend.domain.user.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserInfoRequest {
    private String username;
    private String phoneNumber;
    private String newPhoneNumber;
    private String newUsername;
    private String newNickname;
    @Column(length = 8)
    private String newBirthday;
}
