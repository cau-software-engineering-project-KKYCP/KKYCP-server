package org.kkycp.server.repo.issue;

import lombok.AllArgsConstructor;
import lombok.Data;

public class IssueStatistics {
    @Data
    @AllArgsConstructor
    public static class Time {
        private String timeUnit;
        private Long count;
    }

}
