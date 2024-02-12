package com.starbucks.backend.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starbucks.backend.docs.ApiDocumentUtils;
import com.starbucks.backend.domain.user.dto.ChangePasswordRequest;
import com.starbucks.backend.domain.user.dto.ChangeUserInfoRequest;
import com.starbucks.backend.domain.user.dto.LoginRequest;
import com.starbucks.backend.domain.user.service.UserInfoService;
import com.starbucks.backend.global.jwt.util.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class UserInfoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private ObjectMapper objectMapper;

    private String accessToken;

    @BeforeEach
    public void setUp() throws Exception {
        // 로그인 요청 데이터 생성
        LoginRequest loginRequest = new LoginRequest("wldsmtldsm65@gmail.com", "123qwe!@#QWE");
        String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

        // 로그인 요청 실행
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andReturn();

        String accessTokenFromHeader = Objects.requireNonNull(result.getResponse()
                        .getHeader("Authorization"));

        System.out.println("======================================================");
        System.out.println("accessTokenFromHeader: " + accessTokenFromHeader);
        System.out.println("======================================================");

        accessToken = accessTokenFromHeader;
    }

//    @Test
    @DisplayName("유저 정보 반환 테스트 - 인증 요청 유저와 로그인 한 유저가 같은 경우")
    void GetUserInfoWithRightValues() throws Exception {
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("userId").description("유저 인덱스"),
                                PayloadDocumentation.fieldWithPath("email").description("이메일"),
                                PayloadDocumentation.fieldWithPath("username").description("유저의 실명"),
                                PayloadDocumentation.fieldWithPath("nickname").description("닉네임"),
                                PayloadDocumentation.fieldWithPath("birthday").description("생년월일"),
                                PayloadDocumentation.fieldWithPath("phoneNumber").description("핸드폰 번호")
                        )
                ))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

//    @Test
    @DisplayName("이름 변경 테스트")
    void ChangeUsernameTest() throws Exception {
        ChangeUserInfoRequest changeUserInfoRequest = ChangeUserInfoRequest.builder()
                .username("김시은")
                .phoneNumber("01012345678")
                .newUsername("김지은")
                .build();

        String changeUserInfoRequestJason = objectMapper.writeValueAsString(changeUserInfoRequest);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/user/info/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(changeUserInfoRequestJason))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("username").description("유저 이름"),
                                PayloadDocumentation.fieldWithPath("phoneNumber").description("핸드폰 번호"),
                                PayloadDocumentation.fieldWithPath("newUsername").description("변경 하려는 유저의 실명"),
                                PayloadDocumentation.fieldWithPath("newPhoneNumber").optional().ignored(),
                                PayloadDocumentation.fieldWithPath("newNickname").optional().ignored(),
                                PayloadDocumentation.fieldWithPath("newBirthday").optional().ignored()
                        )
                ))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

//    @Test
    @DisplayName("닉네임 변경 테스트")
    void ChangeNicknameTest() throws Exception {
        ChangeUserInfoRequest changeUserInfoRequest = ChangeUserInfoRequest.builder()
                .username("김시은")
                .phoneNumber("01012345678")
                .newNickname("지니")
                .build();

        String changeUserInfoRequestJason = objectMapper.writeValueAsString(changeUserInfoRequest);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/user/info/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(changeUserInfoRequestJason))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("username").description("유저 이름"),
                                PayloadDocumentation.fieldWithPath("phoneNumber").description("핸드폰 번호"),
                                PayloadDocumentation.fieldWithPath("newNickname").description("변경 하려는 닉네임"),
                                PayloadDocumentation.fieldWithPath("newPhoneNumber").optional().ignored(),
                                PayloadDocumentation.fieldWithPath("newUsername").optional().ignored(),
                                PayloadDocumentation.fieldWithPath("newBirthday").optional().ignored()
                        )
                ))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

//    @Test
//    @DisplayName("휴대폰 번호 변경 테스트")
    void ChangePhoneNumberTest() throws Exception {
        ChangeUserInfoRequest changeUserInfoRequest = ChangeUserInfoRequest.builder()
                .username("김지은")
                .phoneNumber("01012345678")
                .newPhoneNumber("01065535378")
                .build();

        String changeUserInfoRequestJason = objectMapper.writeValueAsString(changeUserInfoRequest);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/user/info/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(changeUserInfoRequestJason))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("username").description("유저 이름"),
                                PayloadDocumentation.fieldWithPath("phoneNumber").description("핸드폰 번호"),
                                PayloadDocumentation.fieldWithPath("newPhoneNumber").optional().description("변경 하려는 핸드폰 번호"),
                                PayloadDocumentation.fieldWithPath("newNickname").optional().ignored(),
                                PayloadDocumentation.fieldWithPath("newUsername").optional().ignored(),
                                PayloadDocumentation.fieldWithPath("newBirthday").optional().ignored()
                        )
                ))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

//    @Test
    @DisplayName("생일 변경 테스트")
    void ChangeBirthdayTest() throws Exception {
        ChangeUserInfoRequest changeUserInfoRequest = ChangeUserInfoRequest.builder()
                .username("김지은")
                .phoneNumber("01012345678")
                .newBirthday("19980406")
                .build();

        String changeUserInfoRequestJason = objectMapper.writeValueAsString(changeUserInfoRequest);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/user/info/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(changeUserInfoRequestJason))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("username").description("유저 이름"),
                                PayloadDocumentation.fieldWithPath("phoneNumber").description("핸드폰 번호"),
                                PayloadDocumentation.fieldWithPath("newBirthday").optional().description("변경 하려는 생년월일"),
                                PayloadDocumentation.fieldWithPath("newPhoneNumber").optional().ignored(),
                                PayloadDocumentation.fieldWithPath("newUsername").optional().ignored(),
                                PayloadDocumentation.fieldWithPath("newNickname").optional().ignored()
                        )
                ))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

//    @Test
    @DisplayName("회원 정보 변경 시, 인증 유저정보와 로그인 유저 정보가 다른 경우")
    void LoginUserIsNotSameWithLoginUser() throws Exception {
        ChangeUserInfoRequest changeUserInfoRequest = ChangeUserInfoRequest.builder()
                .username("김시은")
                .phoneNumber("01065535378")
                .newBirthday("19980406")
                .build();

        String changeUserInfoRequestJason = objectMapper.writeValueAsString(changeUserInfoRequest);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/user/info/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(changeUserInfoRequestJason))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}",
                        ApiDocumentUtils.getDocumentResponse()
                ))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("비밀번호 변경 테스트 - 알맞은 정보 입력")
    void ChangePasswordWithRightValues() throws Exception {
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder()
                .username("김지은")
                .phoneNumber("01065535378")
                .password("qwe123QWE!@#")
                .passwordCheck("qwe123QWE!@#")
                .build();

        String changePasswordRequestJson = objectMapper.writeValueAsString(changePasswordRequest);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/user/info/change/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(changePasswordRequestJson))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("username").description("유저 이름"),
                                PayloadDocumentation.fieldWithPath("phoneNumber").description("핸드폰 번호"),
                                PayloadDocumentation.fieldWithPath("password").description("비밀번호"),
                                PayloadDocumentation.fieldWithPath("passwordCheck").description("확인 비밀번호")
                        )
                ))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("비밀번호 변경 테스트 - 비밀번호와 비밀번호 확인이 같지 않은 경우")
    void passwordNotMatchedPasswordCheck() throws Exception {
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder()
                .username("김지은")
                .phoneNumber("01065535378")
                .password("qwe123QWE!@#")
                .passwordCheck("123qwe!@#QWE")
                .build();

        String changePasswordRequestJson = objectMapper.writeValueAsString(changePasswordRequest);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/user/info/change/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(changePasswordRequestJson))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("{class-name}/{method-name}",
                        ApiDocumentUtils.getDocumentResponse()
                ))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
