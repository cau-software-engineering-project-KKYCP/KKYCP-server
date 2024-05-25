package org.kkycp.server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kkycp.server.controller.admin.AdminController;
import org.kkycp.server.controller.admin.ProjectCreateDto;
import org.kkycp.server.controller.admin.UserRegisterDto;
import org.kkycp.server.services.PrivilegeService;
import org.kkycp.server.services.ProjectService;
import org.kkycp.server.services.UserService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = AdminController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureRestDocs
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private UserService userService;

    @MockBean
    private PrivilegeService privilegeService;

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
                                fieldWithPath("project_name").description(
                                        "추가할 프로젝트 이름. 이미 있는 이름 사용시, 409 Conflict로 에러 json과 함께 응답.")
                        ),
                        responseFields(
                                fieldWithPath("project_id").description("생성된 프로젝트의 ID")
                        )
                )
        );

    }

    @Test
    void addPersonToTheProject() throws Exception {
        Mockito.doNothing().when(userService).registerUserTo(any(Long.class), any(String.class));

        long projectId = 12;
        UserRegisterDto.Request request = new UserRegisterDto.Request();
        request.setUsername("test user");
        mockMvc.perform(post("/project/{projectId}/users", projectId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin/add-person-to-project",
                        pathParameters(
                                parameterWithName("projectId").description(
                                        "유저를 추가할 프로젝트 id.\n만약 해당 id의 프로젝트가 존재하지 않으면, 404 Not found로 응답.")
                        ),
                        requestFields(
                                fieldWithPath("username").description(
                                        "추가할 유저의 username.\n만약 해당 유저가 존재하지 않으면 404 Not found, 이미 해당 유저가 프로젝트에 참가해 있으면 409 Conflict로 응답.")
                        )
                ));
    }
}