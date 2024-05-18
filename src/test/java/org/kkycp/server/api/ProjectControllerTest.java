package org.kkycp.server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kkycp.server.controller.ProjectController;
import org.kkycp.server.controller.dto.ProjectCreateDto;
import org.kkycp.server.services.ProjectService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
public class ProjectControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectService projectService;

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
                                fieldWithPath("project_name").description("추가할 프로젝트 이름")
                        ),
                        responseFields(
                                fieldWithPath("project_id").description("생성된 프로젝트의 ID")
                        )
                )
        );

    }
}
