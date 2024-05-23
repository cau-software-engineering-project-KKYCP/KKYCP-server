package org.kkycp.server.auth;


import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.responseCookies;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.formParameters;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class SignUpAndLoginTest {
    private static final String TEST_USERNAME = "test1";
    private static final String TEST_PASSWORD = "password";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRegisterService registerService;

    @Test
    @Order(1)
    public void signUp() throws Exception {
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", TEST_USERNAME)
                        .param("password", TEST_PASSWORD))
                .andExpect(status().isCreated())
                .andDo(document("auth/signup",
                        formParameters(
                                parameterWithName("username").description("유저 이름 (아이디)"),
                                parameterWithName("password").description("비밀번호")
                        )));
    }

    @Test
    @Order(2)
    public void login() throws Exception {
        registerService.signUp(new RegisterDto.Request(TEST_USERNAME, TEST_PASSWORD));

        String sessionCookieName = "JSESSIONID";
        Cookie sessionCookie = new Cookie(sessionCookieName,
                "vYj_N9coYQuu9QP-Hftf9hHgYNj9C4r6mHVqZ3fc.mptwas2");

        mockMvc.perform(formLogin("/login").user(TEST_USERNAME).password(TEST_PASSWORD))
                .andDo(result -> result.getResponse().addCookie(sessionCookie))
                .andExpect(status().isOk())
                .andDo(document("auth/login",
                        responseCookies(
                                cookieWithName(sessionCookieName).description("쿠키 이름")
                        ),
                        formParameters(
                                parameterWithName("username").description("유저 이름 (아이디)."),
                                parameterWithName("password").description("비밀번호"),
                                parameterWithName("_csrf").ignored()
                        )
                ));
    }

    @Test
    @Order(3)
    public void login_fail() throws Exception {
        registerService.signUp(new RegisterDto.Request(TEST_USERNAME, TEST_PASSWORD));

        mockMvc.perform(formLogin("/login").user(TEST_USERNAME).password("wrong password"))
                .andExpect(status().isUnauthorized());
    }
}


