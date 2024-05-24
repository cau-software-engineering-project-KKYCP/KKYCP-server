package org.kkycp.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_issue_id")
    @Getter(AccessLevel.NONE)
    @Setter
    private Issue parentIssue;

    private String content;

    private LocalDate createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commenter_id")
    private User commenter;

    public Comment(User commenter, String content, LocalDate createdDate) {
        this.content = content;
        this.createdDate = createdDate;
        this.commenter = commenter;
    }

    public void updateContent(String newContent, LocalDate updatedDate) {
        this.content = newContent;
        this.createdDate = updatedDate;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comment comment)) {
            return false;
        }

        return parentIssue.equals(comment.parentIssue) && content.equals(
                comment.content) && createdDate.equals(comment.createdDate) && commenter.equals(
                comment.commenter);
    }

    @Override
    public int hashCode() {
        int result = parentIssue.hashCode();
        result = 31 * result + content.hashCode();
        result = 31 * result + createdDate.hashCode();
        result = 31 * result + commenter.hashCode();
        return result;
    }
}
