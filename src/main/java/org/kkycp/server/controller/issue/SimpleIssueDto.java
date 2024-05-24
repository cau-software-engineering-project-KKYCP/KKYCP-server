package org.kkycp.server.controller.issue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kkycp.server.domain.Issue;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SimpleIssueDto {
    private Long id;
    private String title;
    private LocalDate reportedDate;
    private Issue.Priority priority;
    private Issue.Status status;
}
