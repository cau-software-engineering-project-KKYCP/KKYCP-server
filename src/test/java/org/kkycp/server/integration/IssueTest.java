package org.kkycp.server.integration;

import org.junit.jupiter.api.Test;
import org.kkycp.server.controller.issue.IssueDto;
import org.kkycp.server.controller.issue.IssueUpdateDto;
import org.kkycp.server.domain.Issue;
import org.kkycp.server.repo.issue.TimeUnit;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IssueTest extends FixtureSetupPlatform {
    @Test
    @WithUserDetails(value = "reporter", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void createIssue() throws Exception {
        IssueDto.Request request = new IssueDto.Request("new issue", "issue desc",
                Issue.Priority.CRITICAL, "Bug");
        mockMvc.perform(post("/project/{projectId}/issues", getTestProjectId()).contentType(
                        MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void searchIssue() throws Exception {
        mockMvc.perform(
                        get("/project/{projectId}/issues", getTestProjectId()).queryParam("reporter",
                                testUser.getUsername()).queryParam("status", testIssue.getStatus().name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(testIssue.getId()));
    }

    @Test
    @WithUserDetails(value = "triager", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void assignIssue() throws Exception {
        String title = "assigned issue", desc = "assigned description", assignee = getTestUsername(), type = "Feature";
        Issue.Status status = Issue.Status.ASSIGNED;
        Issue.Priority priority = Issue.Priority.TRIVIAL;

        IssueUpdateDto.Request request = new IssueUpdateDto.Request(title, desc, null, assignee,
                status, priority, type);

        mockMvc.perform(
                        patch("/project/{projectId}/issues/{issueId}", getTestProjectId(), getTestIssueId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        assertThat(testIssue)
                .returns(title, from(Issue::getTitle))
                .returns(desc, from(Issue::getDescription))
                .returns(testUser, from(Issue::getAssignee))
                .returns(status, from(Issue::getStatus))
                .returns(priority, from(Issue::getPriority))
                .returns(type, from(Issue::getType));
    }

    @Test
    @WithUserDetails(value = "test", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void fixIssue() throws Exception {
        testIssue.assignIssue(testUser);

        String fixer = getTestUsername();
        Issue.Status status = Issue.Status.FIXED;

        IssueUpdateDto.Request request = new IssueUpdateDto.Request(null, null, fixer, null,
                status, null, null);

        mockMvc.perform(
                        patch("/project/{projectId}/issues/{issueId}", getTestProjectId(), getTestIssueId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        assertThat(testIssue)
                .returns(testUser, from(Issue::getAssignee))
                .returns(status, from(Issue::getStatus));
    }

    @Test
    void getStatistics() throws Exception {
        mockMvc.perform(get("/project/{projectId}/statistics/time", getTestProjectId())
                        .queryParam("time_unit", TimeUnit.DAY.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].time").exists());
    }


}
