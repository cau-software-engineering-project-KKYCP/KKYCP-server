package org.kkycp.server.services;

import org.kkycp.server.domain.Issue;
import org.kkycp.server.domain.Comment;
import org.kkycp.server.repo.CommentRepo;

public class CommentService {
    private final CommentRepo commentRepo;

    public void sending(long issueid, String content, LocalDate createdDate ,User commenter){
        Issue issue = IssueService.findIssue(issueid);
        Comment comment= new(comment(content, createdDate, commenter));
        commentRepo.StoreComment(issue, comment);
    }

    public void deletion(long issueid, long commentid){
        Issue issue = IssueService.findIssue(issueid);
        commentRepo.DeleteComment(issue, commentid);
    }

    public void modification(long issueid, long commentid, String content){
        Issue issue = IssueService.findIssue(issueid);
        commentRepo.ModificateComment(issue, commentid, content);
    }
}

