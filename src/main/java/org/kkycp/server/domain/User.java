package org.kkycp.server.domain;

import java.util.List;

public class User {
    private Long id;
    private String username;
    private List<Participation> participations;

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User user)) {
            return false;
        }

        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
