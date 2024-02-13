package com.starbucks.backend.global.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SMSVerificationDTO {
    private String username;
    private String phoneNumber;
    private String verificationCode;
}
