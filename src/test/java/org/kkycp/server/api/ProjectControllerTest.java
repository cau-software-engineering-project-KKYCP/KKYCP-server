package org.kkycp.server.api;

import org.junit.jupiter.api.Test;
import org.kkycp.server.controller.ProjectController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ProjectController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureRestDocs
public class ProjectControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void addPersonToTheProject() throws Exception {
        long projectId = 12;
        String username = "test user";
        mockMvc.perform(post("/project/{projectId}/{username}", projectId, username))
                .andExpect(status().isOk())
                .andDo(document("admin/add-person-to-project",
                        pathParameters(
                                parameterWithName("projectId").description(
                                        "유저를 추가할 프로젝트 id.\n만약 해당 id의 프로젝트가 존재하지 않으면, 404 Not found로 응답."),
                                parameterWithName("username").description(
                                        "추가할 유저의 username.\n만약 해당 유저가 존재하지 않으면 404 Not found, 이미 해당 유저가 프로젝트에 참가해 있으면 409 Conflict로 응답."
                                )
                        )));
    }
}
