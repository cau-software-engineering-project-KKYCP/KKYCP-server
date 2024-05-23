package org.kkycp.server.repo;

import org.kkycp.server.domain.Issue;

public interface CommentRepo {
    void StoreComment(Issue issue, long commentid, String comments);
    void DeleteComment(Issue issue, long commentid);
    void ModificateComment(Issue issue, long commentid, String comments);
}
