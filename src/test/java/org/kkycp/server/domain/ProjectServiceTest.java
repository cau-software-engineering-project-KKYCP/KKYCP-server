package org.kkycp.server.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kkycp.server.services.ProjectService;

class ProjectServiceTest {
    private ProjectService service;
    @Test
    void createProject() {
    }

    @Test
    void findProject() {
    }

    @Test
    @DisplayName("addUserTest")
    void addUser() {
        long projectId = 23;
        String username = "윤도경";

        service.addUser(projectId,username);
    }
}