package org.kkycp.server.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kkycp.server.domain.authorization.Privilege;
import org.kkycp.server.repo.UserPrivilegeRecord;
import org.kkycp.server.services.PrivilegeService;
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
    private final PrivilegeService privilegeService;

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
    public List<UserPrivilegeDto.Response> getPrivileges(@PathVariable("project-id") long projectId) {
        List<UserPrivilegeRecord> userPrivileges = privilegeService.getAllUserPrivileges(projectId);
        return userPrivileges.stream()
                .map(p -> new UserPrivilegeDto.Response(p.getUser().getUsername(), p.getPrivileges()))
                .toList();
    }

    @PutMapping("/{project-id}/privileges")
    public void grantUser(@PathVariable("project-id") long projectId,
                          @RequestParam("username") String username,
                          @RequestBody List<Privilege> privileges) {
        userService.replaceUserPrivileges(username, projectId, privileges);
    }
}
