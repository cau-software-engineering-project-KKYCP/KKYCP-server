package org.kkycp.server.repo;

import org.kkycp.server.domain.Project;
import org.kkycp.server.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@RepositoryDefinition(domainClass = User.class, idClass = Long.class)
public interface UserRepo extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    /**
     * 해당 프로젝트에서 할당된 진행중인 이슈가 {@code capAssignedIssues}개 이하인 유저를 모두 가져온다.
     *
     * @param capAssignedIssues 이 숫자 이하로 이슈가 할당 된 사람만 데려온다.
     */
    @Query("select u from User u where (select count(i) from Issue i where i.project = :project and i.assignee = u and i.status = 'ASSIGNED') <= :cap")
    List<User> findAllAvailables(@Param("project") Project targetProject, @Param("cap") int capAssignedIssues);

    @Query("select u from User u join fetch u.projectRegistration.participations where u.username = :username")
    Optional<User> findByUsernameFetchParticipation(@Param("username") String username);

    @Query("select new org.kkycp.server.repo.UserPrivilegeRecord(p.user, p.privileges) from Participation p where p.participatedProject.id = :projectId")
    List<UserPrivilegeRecord> getAllUserPrivileges(@Param("projectId") Long projectId);

    @Query("select u from User u " +
            "join Participation p1 on u = p1.user " +
            "join Project p2 on p1.participatedProject = p2 " +
            "where p2.id = :projectId")
    List<User> findAllByProjectId(Long projectId);
}
