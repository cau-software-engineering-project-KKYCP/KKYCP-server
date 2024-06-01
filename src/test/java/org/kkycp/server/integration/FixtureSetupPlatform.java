package org.kkycp.server.integration;

import org.junit.jupiter.api.BeforeEach;
import org.kkycp.server.auth.jpa.AuthUserDetails;
import org.kkycp.server.auth.jpa.AuthUserDetailsRepo;
import org.kkycp.server.domain.Comment;
import org.kkycp.server.domain.Issue;
import org.kkycp.server.domain.Project;
import org.kkycp.server.domain.User;
import org.kkycp.server.domain.authorization.Privilege;
import org.kkycp.server.repo.CommentRepo;
import org.kkycp.server.repo.ProjectRepo;
import org.kkycp.server.repo.UserRepo;
import org.kkycp.server.repo.issue.IssueRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public abstract class FixtureSetupPlatform extends SessionSharing_SecurityApplied_RollBackAfterTest_Platform {
    @Autowired
    protected AuthUserDetailsRepo authUserDetailsRepo;

    @Autowired
    protected UserRepo userRepo;

    @Autowired
    protected ProjectRepo projectRepo;

    @Autowired
    protected IssueRepo issueRepo;

    @Autowired
    protected CommentRepo commentRepo;

    protected User testUser;
    protected Project testProject;
    protected Issue testIssue;
    protected Comment testComment;

    protected User reporter;
    protected User triager;
    protected User tester;
    protected User verifier;

    @BeforeEach
    void setup() {
        testProject = new Project("test project");
        projectRepo.save(testProject);

        setupUsers();

        testIssue = setupIssue(testUser, "test issue");

        testComment = new Comment(testUser, "comment content", LocalDate.of(2024, 5, 10));
        testIssue.addComment(testComment);
    }

    protected Issue setupIssue(User reporter, String title) {
        Issue issue = new Issue(testProject, reporter, title, "test description",
                Issue.Priority.MAJOR, LocalDate.of(2024, 4, 28), "Bug");
        issueRepo.save(issue);
        return issue;
    }

    private void setupUsers() {
        testUser = setupUserWith("test");
        reporter = setupUserWith("reporter", Privilege.REPORTER);
        triager = setupUserWith("triager", Privilege.TRIAGER);
        tester = setupUserWith("tester", Privilege.TESTER);
        verifier = setupUserWith("verifier", Privilege.VERIFIER);
    }

    protected User setupUserWith(String username, Privilege... privileges) {
        AuthUserDetails authUserDetails = new AuthUserDetails(username, "Bcrypted password");
        authUserDetailsRepo.save(authUserDetails);

        User newUser = new User(username);
        userRepo.save(newUser);
        newUser.participate(testProject);

        for (Privilege privilege : privileges) {
            newUser.addPrivilege(testProject, privilege);
        }
        return newUser;
    }

    protected void setupTestUser(String username) {
        AuthUserDetails authUserDetails = new AuthUserDetails(username, "Bcrypted random password");
        authUserDetailsRepo.save(authUserDetails);

        User user = new User(username);
        userRepo.save(user);
    }

    protected String getTestUsername() {
        return testUser.getUsername();
    }

    protected Long getTestProjectId() {
        return testProject.getId();
    }

    protected Long getTestIssueId() {
        return testIssue.getId();
    }

    protected Long getTestCommentId() {
        return testComment.getId();
    }
}
