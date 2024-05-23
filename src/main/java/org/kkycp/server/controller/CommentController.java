package org.kkycp.server.controller;

import org.kkycp.server.controller.dto.CommentDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController {
    //post comment to the issue
    @PostMapping("/project/{projectId}/issues/{issueId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public void postComment(@PathVariable("projectId") long projectId,
                            @PathVariable("issueId") long issueId,
                            @RequestBody CommentDto.Request request) {

        //TODO
    }

    //put comment
    @PutMapping("/project/{projectId}/issues/{issueId}/comments/{commentId}")
    public void putComment(@PathVariable("projectId") long projectId,
                           @PathVariable("issueId") long issueId,
                           @PathVariable("commentId") long commentId,
                           @RequestBody CommentDto.Request request) {
        //TODO
    }

    @DeleteMapping("/project/{projectId}/issues/{issueId}/comments/{commentId}")
    public void deleteComment(@PathVariable("projectId") long projectId,
                              @PathVariable("issueId") long issueId,
                              @PathVariable("commentId") long commentId) {
        //TODO
    }
}
