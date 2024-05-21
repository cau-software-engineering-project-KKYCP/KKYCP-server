package org.kkycp.server.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Report {
    private User reporter;
    private String title;
    private String description;
    private Issue.Priority priority;
    private String type;
}
