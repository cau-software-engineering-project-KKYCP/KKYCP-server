package org.kkycp.server.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kkycp.server.domain.authorization.Privilege;
import org.kkycp.server.exceptions.UserNotParticipatingException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PrivilegeTest {

    private User user;
    private Project project;

    @BeforeEach
    void setup() {
        user = new User("test user");
        ReflectionTestUtils.setField(user, "id", 1L);
        project = new Project("test project");
        ReflectionTestUtils.setField(project, "id", 1L);
        user.participate(project);
    }

    @Test
    void cannotAddPrivilege_toNonParticipatingUser() {
        User newUser = new User("test user2");
        ReflectionTestUtils.setField(newUser, "id", 2L);

        assertThatThrownBy(() -> newUser.addPrivilege(project, Privilege.TESTER))
                .isInstanceOf(UserNotParticipatingException.class);
    }

    @Test
    void userHasPrivilege() {
        assertThat(user.hasPrivilege(project, Privilege.PARTICIPANT))
                .isTrue();
    }

    @Test
    void replacePrivileges_without_PARTICIPANT_privilege_enforcesItIncluded() {
        user.replacePrivileges(project, List.of(Privilege.VERIFIER, Privilege.REPORTER));
        assertThat(user.hasPrivilege(project, Privilege.PARTICIPANT))
                .isTrue();
    }
}
