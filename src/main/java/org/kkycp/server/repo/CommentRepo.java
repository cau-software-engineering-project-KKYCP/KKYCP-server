package org.kkycp.server.repo;

import org.kkycp.server.domain.Issue;
import org.kkycp.server.domain.Comment;

public interface CommentRepo {
    void StoreComment(Issue issue, Comment comment);
    void DeleteComment(Issue issue, long commentid);
    void ModificateComment(Issue issue, long commentid, String content);
}
