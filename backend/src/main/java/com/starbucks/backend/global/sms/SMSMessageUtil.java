package com.starbucks.backend.global.sms;

import com.starbucks.backend.domain.user.entity.User;
import com.starbucks.backend.domain.user.repository.UserRepository;
import com.starbucks.backend.global.jwt.util.RedisUtil;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.Random;


@Component
@RequiredArgsConstructor
@Slf4j
public class SMSMessageUtil {
    @Value("${coolsms.api.key}")
    private String apikey;

    @Value("${coolsms.api.secret}")
    private String apiSecretKey;

    @Value("${sendNumber}")
    private String sender;


    private DefaultMessageService messageService;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;


    @PostConstruct
    private void init() {
        this.messageService = NurigoApp
                .INSTANCE
                .initialize(apikey, apiSecretKey, "https://api.coolsms.co.kr");
    }

    public void verificationUser(SMSMessageDTO smsMessageDTO){
        String username = smsMessageDTO.username();
        String phoneNumber = smsMessageDTO.phoneNumber();

        Optional<User> user = userRepository.findUserByUsernameAndPhoneNumber(username, phoneNumber);
        if(user.isPresent()) {
            throw new EntityExistsException();
        }
    }

    public SingleMessageSentResponse sendMessage(String sendTo, String verificationCode) {
        Message message = new Message();
        message.setFrom(sender);
        message.setTo(sendTo);
        message.setText("[스타벅스] 인증번호: " + verificationCode);

        SingleMessageSentResponse response = this.messageService
                .sendOne(new SingleMessageSendingRequest(message));

        log.info("======================================================");
        log.info("SMSMessage sent to: " + sendTo);
        log.info("======================================================");

        return response;
    }

    public String generateVerificationCode() {
        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000);

        return String.valueOf(randomNumber);
    }

    public void saveDataForCheckUser(String phoneNumber, String verificationCode) {
        redisUtil.setDataWithExpire(phoneNumber, verificationCode, Duration.ofMinutes(5));
        log.info("======================================================");
        log.info("CheckRedisKeyValue: " + redisUtil.getData(phoneNumber));
        log.info("======================================================");
    }
}
