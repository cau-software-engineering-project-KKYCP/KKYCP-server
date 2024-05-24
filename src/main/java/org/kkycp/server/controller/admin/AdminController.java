package org.kkycp.server.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kkycp.server.domain.authorization.Privilege;
import org.kkycp.server.services.ProjectService;
import org.kkycp.server.services.UserService;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/{project-id}/users")
    public void registerUser(@PathVariable("project-id") long projectId,
                             @RequestBody UserRegisterDto.Request registerDto) {
        userService.registerUserTo(projectId, registerDto.getUsername());
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
