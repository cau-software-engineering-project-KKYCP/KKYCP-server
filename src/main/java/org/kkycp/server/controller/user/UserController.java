package org.kkycp.server.controller.user;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.domain.User;
import org.kkycp.server.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/project/{projectId}/users")
    public List<UserDto.Response> getUsersOfProject(@PathVariable long projectId, @RequestParam(required = false) String username) {
        if (username != null) {
            User user = userService.findByUsernameAndProjectId(username, projectId);
            return List.of(new UserDto.Response(username));
        }

        List<User> users = userService.findAllUserByProjectId(projectId);
        return users.stream().map(u -> new UserDto.Response(u.getUsername())).toList();
    }
}
