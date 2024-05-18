package org.kkycp.server.repo.issue;

import org.kkycp.server.domain.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepo extends JpaRepository<Issue, Long>, IssueSearchRepo{

}
