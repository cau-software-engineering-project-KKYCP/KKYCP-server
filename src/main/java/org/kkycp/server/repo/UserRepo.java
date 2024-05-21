package org.kkycp.server.repo;

import org.kkycp.server.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Optional;

@RepositoryDefinition(domainClass = User.class, idClass = Long.class)
public interface UserRepo extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
