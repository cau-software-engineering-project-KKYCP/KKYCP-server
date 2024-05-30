package org.kkycp.server.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.kkycp.server.controller.admin.ProjectCreateDto;
import org.kkycp.server.controller.admin.UserPrivilegeDto;
import org.kkycp.server.controller.admin.UserRegisterDto;
import org.kkycp.server.controller.project.ProjectDto;
import org.kkycp.server.domain.User;
import org.kkycp.server.domain.authorization.Privilege;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails("admin")
@Transactional
public class AdminTest extends FixtureSetupPlatform {

    @Test
    void getAllProjects() throws Exception {
        MvcResult result = mockMvc.perform(get("/project"))
                .andExpect(status().isOk())
                .andReturn();
        List<ProjectDto.Response> responses = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>(){});

        assertThat(responses)
                .anyMatch(proj -> Objects.equals(proj.getId(), testProject.getId()));
    }

    @Test
    void createProject() throws Exception {
        ProjectCreateDto.Request request = new ProjectCreateDto.Request();
        request.setProjectName("test project");

        mockMvc.perform(post("/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void addUserToTheProject() throws Exception {
        String toBeParticipated = "user to add";
        setupTestUser(toBeParticipated);

        UserRegisterDto.Request request = new UserRegisterDto.Request();
        request.setUsername(toBeParticipated);

        mockMvc.perform(post("/project/{projectId}/users", getTestProjectId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        User participatedUser = userRepo.findByUsername(toBeParticipated).get();
        assertThat(participatedUser.hasPrivilege(testProject, Privilege.PARTICIPANT))
                .isTrue();
    }

    @Test
    void lookupPrivileges() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/project/{projectId}/privileges", getTestProjectId()))
                .andExpect(status().isOk())
                .andReturn();
        List<UserPrivilegeDto.Response> response = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {
                });

        assertThat(response.getFirst().getPrivileges())
                .contains(Privilege.PARTICIPANT);
    }

    @Test
    void resetUsersPrivileges() throws Exception {
        mockMvc.perform(put("/project/{projectId}/privileges?username={username}", getTestProjectId(),
                                getTestUsername())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                        List.of(Privilege.PARTICIPANT, Privilege.VERIFIER))))
                .andExpect(status().isOk());

        testUser.hasPrivilege(testProject, Privilege.PARTICIPANT);
        testUser.hasPrivilege(testProject, Privilege.VERIFIER);
    }
}
