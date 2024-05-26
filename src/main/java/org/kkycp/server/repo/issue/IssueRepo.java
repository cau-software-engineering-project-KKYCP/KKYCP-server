package org.kkycp.server.repo.issue;

import org.kkycp.server.domain.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IssueRepo extends JpaRepository<Issue, Long>, IssueSearchRepo, IssueStatisticsRepo{
    /**
     * statistical method for counting issues between two dates.
     *<p>
     * <b>Watch out that both dates are inclusive. For example, to get the number of issues
     * at the day 2024-05-22, both {@code from} and {@code to} must be 2024-05-22.</b>
     *
     * @param from inclusive date
     * @param to inclusive date
     * @return number of issues between from and to
     */
    @Query("select count(i) from Issue i where i.reportedDate between :from and :to")
    long countBetweenDates(@NonNull @Param("from") LocalDate from,
                           @NonNull @Param("to") LocalDate to);

    List<Issue> findAllByStatus(Issue.Status status);

    List<Issue> findAllByPriority(Issue.Priority priority);

    List<Issue> findAllByType(String string);

    Optional<Issue> findById(long issueId);

    @Query("select i from Issue i " +
            "left join fetch i.reporter " +
            "left join fetch i.fixer " +
            "left join fetch i.assignee " +
            "left join fetch i.comments " +
            "where i.id = :id")
    Optional<Issue> findByIdFetchAll(@Param("id") long issueId);

    @Query("select i from Issue i join fetch i.comments where i.id = :id")
    Issue findByIdFetchComment(@Param("id") Long issueId);


}
