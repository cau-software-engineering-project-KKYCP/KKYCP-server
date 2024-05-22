package org.kkycp.server.repo;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.kkycp.server.domain.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProjectRepoTest {
    @Autowired
    EntityManager em;

    @Autowired
    ProjectRepo sut;

    @Test
    void throw_whenDuplicatedProjectNameInserted() {
        String testProjectName = "test project";
        Project project1 = new Project(testProjectName);
        Project project2 = new Project(testProjectName);

        sut.save(project1);
        sut.save(project2);

        Assertions.assertThatThrownBy(() -> em.flush())
                .isInstanceOf(ConstraintViolationException.class);
    }
}