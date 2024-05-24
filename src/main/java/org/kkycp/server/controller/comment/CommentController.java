package org.kkycp.server.controller.comment;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    //post comment to the issue
    @PostMapping("/project/{projectId}/issues/{issueId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public void postComment(@PathVariable("projectId") long projectId,
                            @PathVariable("issueId") long issueId,
                            @RequestBody CommentDto.Request request) {
        //TODO AUTH
        String commenterName = null;
        commentService.commentIssue(issueId, commenterName, request.getComment(), request.getCreatedDate());
    }

    //put comment
    @PutMapping("/project/{projectId}/issues/{issueId}/comments/{commentId}")
    public void putComment(@PathVariable("projectId") long projectId,
                           @PathVariable("issueId") long issueId,
                           @PathVariable("commentId") long commentId,
                           @RequestBody CommentDto.Request request) {
        //TODO AUTH
        commentService.updateComment(commentId, request.getComment(), request.getCreatedDate());
    }

    @DeleteMapping("/project/{projectId}/issues/{issueId}/comments/{commentId}")
    public void deleteComment(@PathVariable("projectId") long projectId,
                              @PathVariable("issueId") long issueId,
                              @PathVariable("commentId") long commentId) {
        //TODO AUTH
        commentService.deleteComment(issueId, commentId);
    }
}
