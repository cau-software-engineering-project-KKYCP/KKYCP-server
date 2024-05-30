package org.kkycp.server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kkycp.server.controller.admin.AdminController;
import org.kkycp.server.controller.admin.ProjectCreateDto;
import org.kkycp.server.controller.admin.UserRegisterDto;
import org.kkycp.server.domain.User;
import org.kkycp.server.domain.authorization.Privilege;
import org.kkycp.server.repo.UserPrivilegeRecord;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
        ).andDo(document("admin/create-project",
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

    @Test
    public void getPrivileges() throws Exception {
        List<UserPrivilegeRecord> userPrivileges = new ArrayList<>();
        userPrivileges.add(new UserPrivilegeRecord(new User("test user1"), Set.of(Privilege.PARTICIPANT)));
        userPrivileges.add(new UserPrivilegeRecord(new User("test user1"), Set.of(Privilege.PARTICIPANT, Privilege.TRIAGER)));
        userPrivileges.add(new UserPrivilegeRecord(new User("test user1"), Set.of(Privilege.PARTICIPANT, Privilege.TESTER)));
        userPrivileges.add(new UserPrivilegeRecord(new User("test user1"), Set.of(Privilege.PARTICIPANT, Privilege.VERIFIER, Privilege.REPORTER)));


        Mockito.when(privilegeService.getAllUserPrivileges(any(Long.class))).thenReturn(userPrivileges);

        long projectId = 12;
        mockMvc.perform(get("/project/{projectId}/privileges", projectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin/get-privileges",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 id")
                        ),
                        responseFields(
                                fieldWithPath("[].username").description("유저 이름"),
                                fieldWithPath("[].privileges").description("유저 권한")
                        )
                ));
    }

    // 4. Test for grantUser endpoint
    @Test
    public void grantUser() throws Exception {
        Mockito.doNothing().when(userService).replaceUserPrivileges(any(String.class), any(Long.class), any(List.class));

        long projectId = 12;
        String username = "test user";
        List<Privilege> privileges = List.of(Privilege.PARTICIPANT, Privilege.TRIAGER, Privilege.VERIFIER, Privilege.REPORTER);

        mockMvc.perform(put("/project/{projectId}/privileges", projectId)
                        .param("username", username)
                        .content(objectMapper.writeValueAsString(privileges))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin/grant-user",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 id")
                        ),
                        queryParameters(
                                parameterWithName("username").description("유저 이름")
                        ),
                        requestFields(
                                fieldWithPath("[]").description("""
                                        권한의 리스트. PARTICIPANT 권한은 부여하지 않아도 항상 부여된다.
                                        
                                         부여 가능 권한:
                                         PARTICIPANT in the project: 해당 프로젝트의 이슈 생성과 조회, 통계 조회, 코멘트 달기 가능. 자신에게 할당된 이슈를 고칠 수 있음
                                         REPORTER: 해당 프로젝트의 이슈 생성
                                         TRIAGER: 해당 프로젝트의 구성원에게 이슈 할당
                                         TESTER: fixed 상태의 이슈를 resolved 상태로 전환하거나, 해결이 안된 경우 다시 assigned로 전환
                                         VERIFIER: resolved 상태의 이슈를 closed 로 바꿈
                                        """)
                        )
                ));
    }
}
