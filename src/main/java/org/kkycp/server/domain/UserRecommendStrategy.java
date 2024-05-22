package org.kkycp.server.domain;

import java.util.List;

/**
 * Recommendation strategy for triagers to find which user is mostly appropriate to the opened issue.
 */
public interface UserRecommendStrategy {
    List<User> recommendUsers(Issue issue);
}
