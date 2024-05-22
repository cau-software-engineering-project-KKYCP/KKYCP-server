package org.kkycp.server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kkycp.server.controller.ProjectController;
import org.kkycp.server.controller.dto.ProjectCreateDto;
import org.kkycp.server.services.ProjectService;
import org.mockito.Mockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = ProjectController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureRestDocs
public class ProjectControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectService projectService;

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

    @Test
    public void create() throws Exception {
        Mockito.when(projectService.createProject(any(String.class))).thenReturn(1L);

        String testProjectName = "test project";
        ProjectCreateDto.Request request = new ProjectCreateDto.Request();
        request.setProjectName(testProjectName);

        mockMvc.perform(
                post("/project")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(document("project/create",
                        requestFields(
                                fieldWithPath("project_name").description("추가할 프로젝트 이름. 이미 있는 이름 사용시, 409 Conflict로 에러 json과 함께 응답.")
                        ),
                        responseFields(
                                fieldWithPath("project_id").description("생성된 프로젝트의 ID")
                        )
                )
        );

    }
}
