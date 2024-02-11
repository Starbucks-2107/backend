package com.starbucks.backend.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starbucks.backend.docs.ApiDocumentUtils;
import com.starbucks.backend.domain.user.dto.SignUpRequest;
import com.starbucks.backend.domain.user.service.SignUpService;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.ConstraintViolationException;
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
public class SignupControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SignUpService signUpService;

    @Autowired
    private ObjectMapper objectMapper;

//    @Test
    @DisplayName("회원 가입 테스트 - 정상적인 값을 입력한 경우")
    void signupWithRightValues() throws Exception {
        // given
        SignUpRequest signUpRequest = new SignUpRequest(
                "wldsmtldsm65@gmail.com",
                "123qwe!@#QWE",
                "시니",
                "김시은",
                "961120",
                "01012345678"
        );
        String signUpRequestJson = objectMapper.writeValueAsString(signUpRequest);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signUpRequestJson))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("email").description("이메일"),
                                PayloadDocumentation.fieldWithPath("password").description("패스워드"),
                                PayloadDocumentation.fieldWithPath("nickname").description("닉네임"),
                                PayloadDocumentation.fieldWithPath("username").description("유저의 실명"),
                                PayloadDocumentation.fieldWithPath("birthday").description("유저의 생년월일"),
                                PayloadDocumentation.fieldWithPath("phoneNumber").description("핸드폰 번호")
                        )
                ))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

//    @Test
    @DisplayName("회원 가입 테스트 - 이미 있는 유저의 값을 입력한 경우")
    void signupWithExistUserValues() throws Exception {
        // given
        SignUpRequest signUpRequest = new SignUpRequest(
                "wldsmtldsm65@gmail.com",
                "123qwe!@#QWE",
                "시니",
                "김시은",
                "961120",
                "01040365378"
        );
        String signUpRequestJson = objectMapper.writeValueAsString(signUpRequest);

        // when
        Mockito.doThrow(new EntityExistsException())
                .when(signUpService)
                .signUp(Mockito.any(SignUpRequest.class));

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequestJson))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}",
                        ApiDocumentUtils.getDocumentResponse()
                ))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

//    @Test
    @DisplayName("회원 가입 테스트 - 이메일 유효성 검증")
    void signupWithWrongEmail() throws Exception {
        // given
        SignUpRequest signUpRequest = new SignUpRequest(
                "newjeans",
                "123qwe!@#QWE",
                "뭘쳐강",
                "이태훈",
                "960825",
                "01012345678"
        );
        String signUpRequestJson = objectMapper.writeValueAsString(signUpRequest);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequestJson))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}",
                        ApiDocumentUtils.getDocumentResponse()
                ))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

//    @Test
    @DisplayName("회원 가입 테스트 - 비밀번호 유효성 검증")
    void signupWithWrongPassword() throws Exception {
        // given
        SignUpRequest signUpRequest = new SignUpRequest(
                "newjeans@gmail.com",
                "123456789",
                "뭘쳐강",
                "이태훈",
                "960825",
                "01012345678"
        );
        String signUpRequestJson = objectMapper.writeValueAsString(signUpRequest);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequestJson))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}",
                        ApiDocumentUtils.getDocumentResponse()
                ))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

//    @Test
    @DisplayName("회원 가입 테스트 - 닉네임 유효성 검증(한글만)")
    void signupWithEnglishNickname() throws Exception {
        // given
        SignUpRequest signUpRequest = new SignUpRequest(
                "newjeans@gmail.com",
                "123456789",
                "뭘쳐강a",
                "이태훈",
                "960825",
                "01012345678"
        );
        String signUpRequestJson = objectMapper.writeValueAsString(signUpRequest);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequestJson))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}",
                        ApiDocumentUtils.getDocumentResponse()
                ))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

//    @Test
    @DisplayName("회원 가입 테스트 - 닉네임 유효성 검증(6자 이내)")
    void signupWithLongNickname() throws Exception {
        // given
        SignUpRequest signUpRequest = new SignUpRequest(
                "newjeans@gmail.com",
                "123456789",
                "뭘쳐다봐강해린",
                "이태훈",
                "960825",
                "01012345678"
        );
        String signUpRequestJson = objectMapper.writeValueAsString(signUpRequest);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequestJson))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}",
                        ApiDocumentUtils.getDocumentResponse()
                ))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("회원 가입 테스트 - 생년월일 유효성 검증(6자)")
    void signupWithLongBirthday() throws Exception {
        // given
        SignUpRequest signUpRequest = new SignUpRequest(
                "newjeans@gmail.com",
                "123456789",
                "뭘쳐다봐강해린",
                "이태훈",
                "9608257",
                "01012345678"
        );
        String signUpRequestJson = objectMapper.writeValueAsString(signUpRequest);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequestJson))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}",
                        ApiDocumentUtils.getDocumentResponse()
                ))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("회원 가입 테스트 - 생년월일 유효성 검증(6자)")
    void signupWithNotNumberValues() throws Exception {
        // given
        SignUpRequest signUpRequest = new SignUpRequest(
                "newjeans@gmail.com",
                "123456789",
                "뭘쳐다봐강해린",
                "이태훈",
                "96082a",
                "01012345678"
        );
        String signUpRequestJson = objectMapper.writeValueAsString(signUpRequest);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequestJson))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}",
                        ApiDocumentUtils.getDocumentResponse()
                ))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
