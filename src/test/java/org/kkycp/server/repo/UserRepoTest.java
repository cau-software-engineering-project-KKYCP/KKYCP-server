package org.kkycp.server.repo;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kkycp.server.domain.Participation;
import org.kkycp.server.domain.Project;
import org.kkycp.server.domain.User;
import org.kkycp.server.domain.authorization.Privilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepoTest {
    @Autowired
    private EntityManager em;
    @Autowired
    private UserRepo userRepo;

    private User user1;
    private User user2;
    private User user3;
    private Project project1;
    private Project project2;
    private List<Participation> participations;

    @BeforeEach
    void setupEach() {
        project1 = new Project("test project 1");
        project2 = new Project("test project 2");

        em.persist(project1);
        em.persist(project2);

        user1 = new User("test user 1");
        user2 = new User("test user 2");
        user3 = new User("test user 3");

        em.persist(user1);
        em.persist(user2);
        em.persist(user3);

        user1.participate(project1);
        user2.participate(project1);

        user1.participate(project2);
        user2.participate(project2);
        user3.participate(project2);

        user1.replacePrivileges(project1, Set.of(Privilege.PARTICIPANT));
        user2.replacePrivileges(project1, Set.of(Privilege.PARTICIPANT, Privilege.REPORTER));

        assertThat(project1.getId()).isNotNull();
    }

    @Test
    void getAllParticipationOfProject() {
        List<UserPrivilegeRecord> userPrivileges = userRepo.getAllUserPrivileges(project1.getId());
        assertThat(userPrivileges).hasSize(2);
    }
}
