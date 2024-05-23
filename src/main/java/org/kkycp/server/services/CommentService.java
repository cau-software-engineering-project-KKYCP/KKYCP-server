package org.kkycp.server.services;

import org.kkycp.server.domain.Issue;
import org.kkycp.server.repo.CommentRepo;

public class CommentService {
    private final CommentRepo commentRepo;

    public void sending(long issueid, String comments){
        Issue issue = IssueService.findIssue(issueid);
        long commentid;
        commentRepo.StoreComment(issue, commentid, comments);
    }

    public void deletion(long issueid, long commentid){
        Issue issue = IssueService.findIssue(issueid);
        commentRepo.DeleteComment(issue, commentid);
    }

    public void modification(long issueid, long commentid, String comments){
        Issue issue = IssueService.findIssue(issueid);
        commentRepo.ModificateComment(issue, commentid, comments);
    }
}
