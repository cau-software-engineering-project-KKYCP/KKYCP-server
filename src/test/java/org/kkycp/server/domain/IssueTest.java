package org.kkycp.server.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kkycp.server.exceptions.NotAssignedToUserException;
import org.kkycp.server.exceptions.UserNotParticipatingException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

public class IssueTest {

    private Project project;
    private User user;
    private User triager;
    private Issue issue;

    @BeforeEach
    void setup() {
        project = new Project("project");
        setField(project, "id", 1L);

        user = new User("user");
        setField(user, "id", 1L);
        user.participate(project);

        issue = new Issue(project, user, "title", "description", Issue.Priority.TRIVIAL, LocalDate.of(2024, 5, 24), "Bug");
        setField(issue, "id", 1L);
    }

    @Test
    void assignIssue() {
        issue.assignIssue(user);

        assertThat(issue.getAssignee()).isEqualTo(user);
    }

    @Test
    void cannotAssignIssue_toNonParticipatingUser() {
        User newUser = new User("user");
        setField(newUser, "id", 2L);

        assertThatThrownBy(() -> issue.assignIssue(newUser))
                .isInstanceOf(UserNotParticipatingException.class);
    }

    @Test
    void fixIssue() {
        assignIssue();

        issue.fixIssue(user);

        assertThat(issue).returns(user, from(Issue::getFixer));
    }

    @Test
    void cannotBeFixed_byNotAssignedUser() {
        assignIssue();

        User newUser = new User("user");
        setField(newUser, "id", 2L);

        assertThatThrownBy(() -> issue.fixIssue(newUser))
                .isInstanceOf(NotAssignedToUserException.class);
    }
}
