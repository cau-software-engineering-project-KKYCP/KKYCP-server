package org.kkycp.server.integration;

import org.junit.jupiter.api.Test;
import org.kkycp.server.controller.comment.CommentDto;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentTest extends FixtureSetupPlatform {
    @Test
    @WithUserDetails(value = "test", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void createComment() throws Exception {
        CommentDto.Request request = new CommentDto.Request();
        request.setComment("new comment");

        mockMvc.perform(post("/project/{projectId}/issues/{issueId}/comments", getTestProjectId(),
                        getTestIssueId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails(value = "test", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void editComment() throws Exception {
        String editedContent = "edited comment content";
        CommentDto.Request request = new CommentDto.Request();
        request.setComment(editedContent);

        assertThat(testIssue.getComments()).contains(testComment);

        mockMvc.perform(
                        put("/project/{projectId}/issues/{issueId}/comments/{commentId}", getTestProjectId(),
                                getTestIssueId(), getTestCommentId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        assertThat(testIssue.getComments())
                .anyMatch(c -> Objects.equals(c.getId(), getTestCommentId())
                        && c.getContent().equals(editedContent));
    }

    @Test
    @WithUserDetails(value = "test", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deleteComment() throws Exception {
        Long commentId = getTestCommentId();

        assertThat(testIssue.getComments()).contains(testComment);

        mockMvc.perform(delete("/project/{projectId}/issues/{issueId}/comments/{commentId}",
                        getTestProjectId(), getTestIssueId(), commentId))
                .andExpect(status().isOk());

        assertThat(testIssue.getComments())
                .doesNotContain(testComment);
    }
}
