package org.kkycp.server.repo.issue;

import lombok.AllArgsConstructor;
import lombok.Data;

public class IssueStatistics {
    @Data
    @AllArgsConstructor
    public static class Time {
        //year, month, day format are different
        private Object time;
        private Long count;
    }

}
