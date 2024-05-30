package org.kkycp.server.integration;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class UserTest extends FixtureSetupPlatform {
    @Test
    void findAnUser() throws Exception {
        mockMvc.perform(get("/project/{projectId}/users", getTestProjectId())
                .queryParam("username", "test"))
                .andExpect(jsonPath("$.[?(@.username == 'test')]").exists());
    }

    @Test
    void findAllUsers() throws Exception {
        mockMvc.perform(get("/project/{projectId}/users", getTestProjectId()))
                .andExpect(jsonPath("$.[*].username").isNotEmpty());
    }
}
