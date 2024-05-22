package org.kkycp.server.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private Project project;
    private User user;
    private ProjectRegistration  projectRegistrations;

    @BeforeEach
    void setUp() throws Exception { //
        project = new Project();
        user = new User();

        project.setProjectName("anythingELse");
    }

    @Test
    void TestParticipate() {
        // 참여하기 전에는 사용자가 프로젝트에 등록되지 않았을 것
        assertFalse(user.getProjectRegistration().isRegistered(project));
        assertFalse(user.isParticipated(project));

        // 사용자를 프로젝트에 참여시킴
        user.participate(project);

        // 참여 후에는 사용자가 프로젝트에 등록되었을 것
        assertTrue(user.getProjectRegistration().isRegistered(project));
        assertTrue(user.isParticipated(project));
    }

}