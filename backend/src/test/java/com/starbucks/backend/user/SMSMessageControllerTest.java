package com.starbucks.backend.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starbucks.backend.docs.ApiDocumentUtils;
import com.starbucks.backend.global.sms.SMSMessageDTO;
import com.starbucks.backend.global.sms.SMSMessageService;
import com.starbucks.backend.global.sms.SMSVerificationRequest;
import jakarta.persistence.EntityExistsException;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class SMSMessageControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SMSMessageService smsMessageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("문자 발신 테스트 - 회원 가입을 하지 않은 유저")
    void sendSMSToUser() throws Exception {
        // given
        SMSMessageDTO smsMessageDTO = new SMSMessageDTO("김시은", "01065535378");
        String smsMessageJson = objectMapper.writeValueAsString(smsMessageDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/sms/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(smsMessageJson))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("username").description("유저의 실명"),
                                PayloadDocumentation.fieldWithPath("phoneNumber").description("핸드폰 번호")
                        )
                ))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(smsMessageJson));
    }

    @Test
    @DisplayName("문자 발신 테스트 - 이미 존재 하는 유저")
    void sendSMSToExistUser() throws Exception {
        // given
        SMSMessageDTO smsMessageDTO = new SMSMessageDTO("김시은", "01065535378");
        String smsMessageJson = objectMapper.writeValueAsString(smsMessageDTO);

        // when
        Mockito.doThrow(new EntityExistsException())
                .when(smsMessageService)
                .getSMSVerificationBeforeSignUp(smsMessageDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/sms/send")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(smsMessageJson))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("username").description("유저의 실명"),
                                PayloadDocumentation.fieldWithPath("phoneNumber").description("핸드폰 번호")
                        )
                ))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("인증번호 확인 테스트 - 인증번호를 알맞게 입력한 경우")
    void verificationWithRightCode() throws Exception {
        //given
        SMSVerificationRequest smsVerificationRequest =
                new SMSVerificationRequest("김시은", "01065535378", "6960");
        String smsVerificationRequestJson = objectMapper.writeValueAsString(smsVerificationRequest);

        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/sms/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(smsVerificationRequestJson))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("username").description("유저의 실명"),
                                PayloadDocumentation.fieldWithPath("phoneNumber").description("핸드폰 번호"),
                                PayloadDocumentation.fieldWithPath("verificationCode").description("인증 번호")
                        )
                ))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(smsVerificationRequestJson));
    }

    @Test
    @DisplayName("인증번호 확인 테스트 - 인증번호를 잘못 입력한 경우")
    void verificationWithWrongCode() throws Exception {
        //given
        SMSVerificationRequest smsVerificationRequest =
                new SMSVerificationRequest("김시은", "01065535378", "6961");
        String smsVerificationRequestJson = objectMapper.writeValueAsString(smsVerificationRequest);

        // when
        Mockito.doThrow(new BadRequestException())
                .when(smsMessageService)
                .checkUserUsingVerificationCode(
                        smsVerificationRequest.getPhoneNumber(),
                        smsVerificationRequest.getVerificationCode());

        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/sms/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(smsVerificationRequestJson))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("username").description("유저의 실명"),
                                PayloadDocumentation.fieldWithPath("phoneNumber").description("핸드폰 번호"),
                                PayloadDocumentation.fieldWithPath("verificationCode").description("인증 번호")
                        )
                ))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
