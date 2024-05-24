package org.kkycp.server.learning;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.kkycp.server.domain.Project;
import org.kkycp.server.domain.User;
import org.kkycp.server.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class JpaMapKeyTest {
    @Autowired
    EntityManager em;

    @Autowired
    UserRepo userRepo;

    @Test
    void test() {
        User user1 = new User("user1");
        Project project1 = new Project("project1");
        Project project2 = new Project("project2");
        user1.register(project1);
        user1.register(project2);

        em.persist(project1);
        em.persist(project2);
        em.persist(user1);
        em.flush();
        em.clear();

        User foundUser = userRepo.findByUsername("user1").orElseThrow();
        assertThat(foundUser.isParticipated(project1)).isTrue();
        assertThat(foundUser.isParticipated(project2)).isTrue();
    }
}
