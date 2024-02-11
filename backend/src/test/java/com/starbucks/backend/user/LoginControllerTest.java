package com.starbucks.backend.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starbucks.backend.docs.ApiDocumentUtils;
import com.starbucks.backend.domain.user.dto.LoginRequest;
import com.starbucks.backend.domain.user.service.LoginService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class LoginControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    LoginService loginService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("로그인 테스트 - 정상적인 값을 입력한 경우")
    void loginWithRightValues() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest(
                "wldsmtldsm65@gmail.com",
                "123qwe!@#QWE"
        );
        String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("email").description("이메일"),
                                PayloadDocumentation.fieldWithPath("password").description("비밀번호")
                        ),
                        HeaderDocumentation.responseHeaders(
                                HeaderDocumentation.headerWithName("Authorization").description("AccessToken"),
                                HeaderDocumentation.headerWithName("Set-Cookie").description("refreshToken")
                        )
                ))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("로그인 테스트 - 존재 하지 않는 이메일을 입력한 경우")
    void loginWithWrongEmail() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest(
                "suminnnn@gmail.com",
                "123qwe!@#QWE"
        );
        String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("email").description("이메일"),
                                PayloadDocumentation.fieldWithPath("password").description("비밀번호")
                        )
                ))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @DisplayName("로그인 테스트 - 잘못된 비밀번호를 입력한 경우")
    void loginWithWrongPassword() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest(
                "wldsmtldsm65@gmail.com",
                "123456789"
        );
        String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("email").description("이메일"),
                                PayloadDocumentation.fieldWithPath("password").description("비밀번호")
                        )
                ))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
