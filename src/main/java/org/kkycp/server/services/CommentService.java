package org.kkycp.server.services;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.domain.Issue;
import org.kkycp.server.domain.Comment;
import org.kkycp.server.domain.User;
import org.kkycp.server.repo.CommentRepo;
import org.kkycp.server.repo.UserRepo;
import org.kkycp.server.repo.issue.IssueRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final IssueRepo issueRepo;
    private final CommentRepo commentRepo;
    private final UserRepo userRepo;

    public void commentIssue(long issueid, String commenterName, String content, LocalDate createdDate){
        Issue issue = issueRepo.findById(issueid).get();
        User commenter = userRepo.findByUsername(commenterName).get();
        Comment comment= new Comment(commenter, content, createdDate);
        issue.addComment(comment);
    }

    public void deleteComment(long issueId, long commentId){
        Issue commentedIssue = issueRepo.findById(issueId).get();
        Comment comment = commentRepo.findById(commentId).get();
        commentedIssue.deleteComment(comment);
    }

    public void updateComment(long commentId, String content, LocalDate localDate){
        Comment comment = commentRepo.findById(commentId).get();
        comment.updateContent(content, localDate);
    }
}

