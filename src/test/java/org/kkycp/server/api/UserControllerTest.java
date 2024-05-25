package org.kkycp.server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kkycp.server.controller.user.UserController;
import org.kkycp.server.domain.User;
import org.kkycp.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = UserController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setup() {
        User user = new User("testUser");
        when(userService.findAllUserByProjectId(1L)).thenReturn(Collections.singletonList(user));
    }

    @Test
    public void testGetUsersOfProjectWithUsername() throws Exception {
        String username = "testUser";
        User user = new User(username);
        when(userService.findByUsernameAndProjectId(username, 1L)).thenReturn(user);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/project/{projectId}/users", 1L)
                        .param("username", username))
                .andExpect(status().isOk())
                .andDo(document("get-users-of-project-with-username",
                        queryParameters(
                                parameterWithName("username").description("찾는 유저의 이름. 만약 해당 유저가 없다면 404 Not Found로 응답한다.")
                        ),
                        responseFields(
                                // Document the fields in the response
                                fieldWithPath("[].username").description("찾는 유저의 이름 리스트. 한명이기 때문에 하나의 요소만 들어있다.")
                        )));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        User user1 = new User("testUser1");
        User user2 = new User("testUser2");
        when(userService.findAllUserByProjectId(1L)).thenReturn(List.of(user1, user2));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/project/{projectId}/users", 1L))
                .andExpect(status().isOk())
                .andDo(document("get-users-of-project-without-username",
                        responseFields(
                                fieldWithPath("[].username").description("찾는 유저의 이름 리스트. 한명 이상일 수 있다.")
                        )));
    }
}