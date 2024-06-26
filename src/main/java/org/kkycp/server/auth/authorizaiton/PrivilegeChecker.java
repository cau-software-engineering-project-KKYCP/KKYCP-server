package org.kkycp.server.auth.authorizaiton;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.auth.jpa.AuthUserDetails;
import org.kkycp.server.domain.*;
import org.kkycp.server.domain.authorization.Privilege;
import org.kkycp.server.exceptions.UserNotParticipatingException;
import org.kkycp.server.repo.CommentRepo;
import org.kkycp.server.repo.ParticipationRepo;
import org.kkycp.server.repo.ProjectRepo;
import org.kkycp.server.repo.UserRepo;
import org.kkycp.server.repo.issue.IssueRepo;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.BooleanSupplier;

@Component("authz")
@RequiredArgsConstructor
@Transactional
public class PrivilegeChecker {
    private final UserRepo userRepo;
    private final CommentRepo commentRepo;
    private final ProjectRepo projectRepo;
    private final IssueRepo issueRepo;
    private final ParticipationRepo participationRepo;

    private boolean doBasicAuthWith(MethodSecurityExpressionOperations operations, BooleanSupplier customChecker) {
        if (!operations.isAuthenticated() || operations.isAnonymous()) {
            return false;
        }

        if (operations.hasRole("ADMIN")) {
            return true;
        }

        return customChecker.getAsBoolean();
    }

    public boolean hasPrivilege(MethodSecurityExpressionOperations operations, long projectId, Privilege requiredPrivilege) {
        return doBasicAuthWith(operations, () -> {
            String username = getUsername(operations);

            Participation participation = participationRepo.findParticipation(username, projectId)
                    .orElseThrow(UserNotParticipatingException::new);
            return participation.hasPrivilege(requiredPrivilege);
        });
    }

    private static String getUsername(MethodSecurityExpressionOperations operations) {
        AuthUserDetails authUser = (AuthUserDetails) operations.getAuthentication().getPrincipal();
        return authUser.getUsername();
    }

    public boolean isCommentOwner(MethodSecurityExpressionOperations operations, long commentId) {
        return doBasicAuthWith(operations, () -> {
            Comment comment = commentRepo.findById(commentId).get();
            User user = getUser(operations);
            return comment.getCommenter() == user;
        });
    }

    public boolean isReportedByPrincipal(MethodSecurityExpressionOperations operations, long issueId) {
        return doBasicAuthWith(operations, () -> {
            Issue issue = issueRepo.findById(issueId).get();
            User user = getUser(operations);
            return issue.getReporter() == user;
        });
    }

    public boolean isAssignedToPrincipal(MethodSecurityExpressionOperations operations, long issueId) {
        return doBasicAuthWith(operations, () -> {
            Issue issue = issueRepo.findById(issueId).get();
            User user = getUser(operations);
            return issue.getAssignee() == user;
        });
    }

    private User getUser(MethodSecurityExpressionOperations operations) {
        User user = userRepo.findByUsername(getUsername(operations)).get();
        return user;
    }

    public boolean canChangeIssueStatus(MethodSecurityExpressionOperations operations, long projectId, Issue.Status desiredStatus) {
        return doBasicAuthWith(operations, () -> {
            User user = getUser(operations);
            Project project = projectRepo.findById(projectId).get();

            return user.hasPrivilege(project, Privilege.TESTER)
                    || user.hasPrivilege(project, Privilege.VERIFIER);
        });
    }
}
