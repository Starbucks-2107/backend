package com.starbucks.backend.global.util;

import org.springframework.stereotype.Component;

@Component
public class MaskingUtil {
    public String maskingEmail(String email) {
        String[] parts = email.split("@");
        String localPart = parts[0]; // "@" 앞부분
        String domainPart = parts[1]; // "@" 뒷부분

        // 앞 부분의 50%를 마스킹 처리
        int maskLength = localPart.length() / 2;
        String maskedPart = localPart.substring(maskLength + 1).replaceAll(".", "*");
        return localPart.substring(0, maskLength - 1) + maskedPart + "@" + domainPart;
    }

    public String maskingPhoneNumber(String phoneNumber) {
        return "010****" + phoneNumber.substring(7);
    }

    public String maskingUsername(String username) {
        int length = username.length();

        if (length == 1) {
            return username;

        } else if (length == 2) {
            return username.charAt(0) + "*";

        } else {
            int middle = length / 2;
            if (length % 2 == 0) {
                return username.substring(0, middle - 1) + "**" + username.substring(middle + 1);

            } else {
                return username.substring(0, middle) + "*" + username.substring(middle + 1);
            }
        }
    }

    public String maskingBirthday(String birthday) {
        return "****" + birthday.substring(4);
    }
}
