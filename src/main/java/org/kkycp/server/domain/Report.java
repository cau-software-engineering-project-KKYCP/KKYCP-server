package org.kkycp.server.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Report {
    private String title;
    private String description;
    private User reporter;
    private Issue.Priority priority;
    private String type;
}
