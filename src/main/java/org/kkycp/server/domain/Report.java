package org.kkycp.server.domain;

import lombok.Data;

@Data
public class Report {
    private User reporter;
    private String title;
    private String description;
    private Issue.Priority priority;
}
