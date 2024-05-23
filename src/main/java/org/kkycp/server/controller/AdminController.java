package org.kkycp.server.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kkycp.server.controller.dto.ProjectCreateDto;
import org.kkycp.server.controller.dto.UserPrivilegeDto;
import org.kkycp.server.domain.authorization.Privilege;
import org.kkycp.server.services.ProjectService;
import org.kkycp.server.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class AdminController {
    private final ProjectService projectService;
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectCreateDto.Response createProject(
            @Valid @RequestBody ProjectCreateDto.Request dto) {
        long createdProjectId = projectService.createProject(dto.getProjectName());
        return new ProjectCreateDto.Response(createdProjectId);
    }

    @PostMapping("/{project-id}/{username}")
    public void registerUser(@PathVariable("project-id") long projectId,
                             @PathVariable("username") String username) {
        userService.registerUserTo(projectId, username);
    }

    @GetMapping("/{project-id}/privileges")
    public List<UserPrivilegeDto.Response> getPrivileges(@PathVariable("project-id") long projectId,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "50") int size) {
        //TODO
        return null;
    }

    @PutMapping("/{project-id}/privileges/{username}")
    @ResponseStatus(HttpStatus.CREATED)
    public void grantUser(@PathVariable("project-id") long projectId,
                          @PathVariable("username") String username,
                          @RequestBody List<Privilege> privileges) {
        //TODO
    }
}
