package org.kkycp.server.repo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.kkycp.server.domain.Participation;

import java.util.List;

import static org.kkycp.server.domain.QParticipation.participation;

public class UserPrivilegeRepoImpl implements UserPrivilegeRepo {
    private final JPAQueryFactory queryFactory;

    public UserPrivilegeRepoImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<UserPrivilegeRecord> getAllUserPrivileges(Long projectId) {
        List<Participation> projectParticipations = queryFactory.selectFrom(participation)
                .from(participation)
                .where(participation.participatedProject.id.eq(projectId))
                .fetch();
        return projectParticipations.stream()
                .map(p -> new UserPrivilegeRecord(p.getUser(), p.getPrivileges()))
                .toList();
    }
}
