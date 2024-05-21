package org.kkycp.server.repo.issue;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.NonNull;
import org.kkycp.server.domain.Issue;

import java.util.List;

import static org.kkycp.server.domain.QIssue.issue;
import static org.springframework.util.StringUtils.hasText;

public class IssueSearchRepoImpl implements IssueSearchRepo {
    private final JPAQueryFactory queryFactory;

    public IssueSearchRepoImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Issue> search(@NonNull Long projectId, IssueSearchCondition searchCondition) {
        return queryFactory.selectFrom(issue)
                .where(projectIdEq(projectId))
                .where(titleLike(searchCondition.getTitle()),
                        assigneeNameEq(searchCondition.getAssigneeName()),
                        reporterNameEq(searchCondition.getReporterName()),
                        priorityEq(searchCondition.getPriority()),
                        statusEq(searchCondition.getStatus()),
                        typeEq(searchCondition.getType()))
                .fetch();
    }

    private BooleanExpression projectIdEq(Long projectId) {
        return projectId != null ? issue.project.id.eq(projectId) : null;
    }

    private BooleanExpression titleLike(String title) {
        return hasText(title) ? issue.title.like("%"+title+"%") : null;
    }

    private BooleanExpression assigneeNameEq(String assigneeName) {
        return hasText(assigneeName) ? issue.assignee.username.eq(assigneeName) : null;
    }

    private BooleanExpression reporterNameEq(String reporterName) {
        return hasText(reporterName) ? issue.reporter.username.eq(reporterName) : null;
    }

    private BooleanExpression priorityEq(Issue.Priority priority) {
        return priority != null ? issue.priority.eq(priority) : null;
    }

    private BooleanExpression statusEq(Issue.Status status) {
        return status != null ? issue.status.eq(status) : null;
    }

    private BooleanExpression typeEq(String type) {
        return hasText(type) ? issue.type.eq(type) : null;
    }


}
