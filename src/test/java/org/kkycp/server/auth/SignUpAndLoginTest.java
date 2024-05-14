package org.kkycp.server.auth;


import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.kkycp.server.RestDocsTestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.formParameters;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SignUpAndLoginTest extends RestDocsTestTemplate {
    private static final String TEST_USERNAME = "test1";
    private static final String TEST_PASSWORD = "password";

    @Test
    @Order(1)
    @Transactional
    public void signUp() throws Exception {
        mockMvc.perform(post("/signup")
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
    public void test() throws Exception {
        mockMvc.perform(get("/test").with(httpBasic(TEST_USERNAME, TEST_PASSWORD)))
                .andExpect(status().isOk())
                .andDo(document("auth/login", requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION)
                                .description("HTTP Basci Authentication 헤더. https://en.wikipedia.org/wiki/Basic_access_authentication#Client_side 참고")
                )));
    }
}
