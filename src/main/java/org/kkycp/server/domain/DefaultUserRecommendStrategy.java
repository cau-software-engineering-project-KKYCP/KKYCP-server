package org.kkycp.server.domain;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.repo.UserRepo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultUserRecommendStrategy implements UserRecommendStrategy{
    private final UserRepo userRepo;

    @Override
    public List<User> recommendUsers(Issue issue) {
        //TODO
        return List.of();
    }
}
