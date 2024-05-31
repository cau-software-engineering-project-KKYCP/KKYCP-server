package org.kkycp.server.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.kkycp.server.controller.issue.IssueDto;
import org.kkycp.server.controller.issue.IssueUpdateDto;
import org.kkycp.server.controller.issue.SimpleIssueDto;
import org.kkycp.server.domain.Issue;
import org.kkycp.server.domain.User;
import org.kkycp.server.repo.issue.TimeUnit;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

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
    void searchIssue_ofTheReporter() throws Exception {
        List<Issue> testIssues = List.of(setupIssue(testUser, "test1"), setupIssue(testUser, "test2"),
                setupIssue(testUser, "test3"), setupIssue(testUser, "test4"));
        User assignee = setupUserWith("tester assignee1");

        for (int i = 0; i < 2; i++) {
            Issue issue = testIssues.get(i);
            issue.assignIssue(assignee);
        }

        MvcResult result = mockMvc.perform(
                        get("/project/{projectId}/issues", getTestProjectId())
                                .queryParam("limit", "20")
                                .queryParam("reporter", testUser.getUsername()))
                .andExpect(status().isOk())
                .andReturn();

        List<SimpleIssueDto> responses = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(responses).extracting(SimpleIssueDto::getId)
                .contains(testIssue.getId())
                .contains(testIssues.get(0).getId(), testIssues.get(1).getId(),
                        testIssues.get(2).getId(), testIssues.get(3).getId());
    }

    @Test
    void searchIssue_ofTheAssignee() throws Exception {
        List<Issue> testIssues = List.of(setupIssue(testUser, "test1"), setupIssue(testUser, "test2"),
                setupIssue(testUser, "test3"), setupIssue(testUser, "test4"));
        User assignee = setupUserWith("tester assignee1");

        for (int i = 0; i < 2; i++) {
            Issue issue = testIssues.get(i);
            issue.assignIssue(assignee);
        }

        MvcResult result = mockMvc.perform(
                        get("/project/{projectId}/issues", getTestProjectId())
                                .queryParam("assignee", assignee.getUsername()))
                .andExpect(status().isOk())
                .andReturn();

        List<SimpleIssueDto> responses = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(responses).extracting(SimpleIssueDto::getId)
                .contains(testIssues.get(0).getId(), testIssues.get(1).getId())
                .doesNotContain(testIssues.get(2).getId(), testIssues.get(3).getId());

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
    @WithUserDetails(value = "tester", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void closeIssue() throws Exception {
        testIssue.assignIssue(testUser);
        testIssue.fixIssue(testUser);

        Issue.Status status = Issue.Status.RESOLVED;
        mockMvc.perform(
                        patch("/project/{projectId}/issues/{issueId}", getTestProjectId(), getTestIssueId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new IssueUpdateDto.Request(null, null, null, null, status, null, null)))
                )
                .andExpect(status().isOk());

        assertThat(testIssue.getStatus()).isEqualTo(status);
    }

    @Test
    void getStatistics() throws Exception {
        mockMvc.perform(get("/project/{projectId}/statistics/time", getTestProjectId())
                        .queryParam("time_unit", TimeUnit.DAY.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].time").exists());
    }


}
