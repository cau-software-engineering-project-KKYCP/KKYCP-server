package org.kkycp.server.controller.project;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.services.ProjectService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping("/project")
    public List<ProjectDto.Response> getAllProjects() {
        return projectService.findAllProjects().stream()
                .map(p -> new ProjectDto.Response(p.getId(), p.getProjectName()))
                .toList();
    }
}
