package org.kkycp.server.integration;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.kkycp.server.controller.admin.ProjectCreateDto;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SessionTest extends SessionSharing_SecurityApplied_RollBackAfterTest_Platform {

    @Test
    @Order(0)
    void test1() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "admin")
                        .param("password", "admin"))
                .andExpect(status().isOk());

        ProjectCreateDto.Request request = new ProjectCreateDto.Request();
        request.setProjectName("test project");

        mockMvc.perform(post("/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(1)
    void test2() throws Exception {
        ProjectCreateDto.Request request = new ProjectCreateDto.Request();
        request.setProjectName("test project");

        mockMvc.perform(post("/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    void test3() throws Exception {
        mockMvc.perform(post("/logout"))
                .andExpect(status().isOk());
    }

}

