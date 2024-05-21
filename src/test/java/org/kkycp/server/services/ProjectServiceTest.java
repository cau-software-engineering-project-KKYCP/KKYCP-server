package org.kkycp.server.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kkycp.server.domain.Project;
import org.kkycp.server.domain.User;

import static org.junit.jupiter.api.Assertions.*;

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