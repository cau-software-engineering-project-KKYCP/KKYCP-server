package org.kkycp.server.repo.issue;

import lombok.NonNull;
import org.kkycp.server.domain.Issue;

import java.util.List;

public interface IssueSearchRepo {
    /**
     * @param issueSearchCondition for null fields, all subject is found
     * @return list of found object. Empty if there's no issue meeting the condition
     */
    List<Issue> search(@NonNull Long projectId, IssueSearchCondition issueSearchCondition);
}
