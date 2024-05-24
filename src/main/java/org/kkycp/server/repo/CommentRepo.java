package org.kkycp.server.repo;

import org.kkycp.server.domain.Issue;
import org.kkycp.server.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepo extends JpaRepository<Comment, Long> {
}
