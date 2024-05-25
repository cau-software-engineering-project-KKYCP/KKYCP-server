package org.kkycp.server.repo.issue;

import java.util.List;

public interface IssueStatisticsRepo {
    List<IssueStatistics.Time> countIssueGroupByTime(TimeUnit timeUnit);
}
