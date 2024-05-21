package org.kkycp.server.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
public class ProjectController {
    @PostMapping("/{project-id}/{username}")
    public void registerUser(@PathVariable("project-id") long projectId,
                             @PathVariable("username") String username) {
        //TODO: combine with user service with admin authorization check
    }
}
