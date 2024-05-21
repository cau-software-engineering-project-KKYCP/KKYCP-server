package org.kkycp.server.repo.issue;

import org.kkycp.server.domain.Issue;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Issue.class, idClass = Long.class)
public interface IssueRepo extends CrudRepository<Issue, Long>, IssueSearchRepo {
}
