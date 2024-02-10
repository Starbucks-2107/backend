package com.starbucks.backend.global.sms;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/auth/sms")
@RequiredArgsConstructor
public class SMSController {
    private final SMSMessageService smsMessageService;

    @PostMapping(value = "/send")
    public ResponseEntity<?> sendSMSForVerification(@RequestBody @Valid SMSMessageDTO smsMessageDTO) throws Exception {
        smsMessageService.getSMSVerificationBeforeSignUp(smsMessageDTO);
        return ResponseEntity.ok(smsMessageDTO);
    }

    @PostMapping(value = "/check")
    public ResponseEntity<?> checkUserUsingVerificationCode(@RequestBody SMSVerificationRequest smsVerificationRequest) throws Exception {
        smsMessageService.checkUserUsingVerificationCode(smsVerificationRequest.getPhoneNumber()
                , smsVerificationRequest.getVerificationCode());

        return ResponseEntity.ok(smsVerificationRequest);
    }
}
