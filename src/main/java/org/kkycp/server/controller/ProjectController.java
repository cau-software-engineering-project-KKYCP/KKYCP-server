package org.kkycp.server.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kkycp.server.controller.dto.ProjectCreateDto;
import org.kkycp.server.services.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectCreateDto.Response createProject(@Valid @RequestBody ProjectCreateDto.Request dto) {
        long createdProjectId = projectService.createProject(dto.getProjectName());
        return new ProjectCreateDto.Response(createdProjectId);
    }

    @PostMapping("/{project-id}/{username}")
    public void registerUser(@PathVariable("project-id") long projectId,
                             @PathVariable("username") String username) {
        //TODO: combine with user service with admin authorization check
    }
}
