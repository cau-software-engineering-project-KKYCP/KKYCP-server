package org.kkycp.server.repo;

import org.kkycp.server.domain.Participation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@RepositoryDefinition(domainClass = Participation.class, idClass = Long.class)
public interface ParticipationRepo {
    @Query("select p from Participation p " +
            "join p.user u " +
            "join p.participatedProject pp " +
            "where u.username = :username and pp.id = :projectId")
    Optional<Participation> findParticipation(
            @Param("username") String username, @Param("projectId") long projectId);
}
