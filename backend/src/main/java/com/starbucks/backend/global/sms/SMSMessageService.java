package com.starbucks.backend.global.sms;

import com.starbucks.backend.global.jwt.util.RedisUtil;
import com.starbucks.backend.global.sms.dto.SMSMessageDTO;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SMSMessageService {
    private final SMSMessageUtil smsMessageUtil;
    private final RedisUtil redisUtil;

    public void getSMSVerificationBeforeSignUp(SMSMessageDTO smsMessageDTO){
        smsMessageUtil.verificationUser(smsMessageDTO);

        String verificationCode = smsMessageUtil.generateVerificationCode();
        String phoneNumber = smsMessageDTO.phoneNumber();

        smsMessageUtil.sendMessage(phoneNumber, verificationCode);
        smsMessageUtil.saveDataForCheckUser(phoneNumber, verificationCode);
    }

    public void checkUserUsingVerificationCode(String phoneNumber, String verificationCode) throws BadRequestException {
        String redisValue = redisUtil.getData(phoneNumber);
        if (!redisValue.equals(verificationCode)) {
          throw new BadRequestException();
        }
    }
}
