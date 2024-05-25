package org.kkycp.server.repo.issue;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static org.kkycp.server.domain.QIssue.issue;

public class IssueStatisticsRepoImpl implements IssueStatisticsRepo {
    private final JPAQueryFactory queryFactory;

    public IssueStatisticsRepoImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<IssueStatistics.Time> countIssueGroupByTime(TimeUnit timeUnit) {
        return queryFactory.select(Projections.constructor(IssueStatistics.Time.class,
                                new Class[]{String.class, Long.class},
                                timeUnit.groupBy(issue.reportedDate), issue.count()))
                .from(issue).fetch();
    }
}
