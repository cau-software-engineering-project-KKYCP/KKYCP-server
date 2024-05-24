package org.kkycp.server.controller.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @GetMapping("/project/{projectId}/users")
    public List<UserDto.Response> getUsersOfProject(@PathVariable String projectId, @RequestParam String username) {
        //TODO
        return null;
    }
}
