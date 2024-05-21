package org.kkycp.server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
public class User{
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    @Embedded
    private ProjectRegistration projectRegistration;

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User user)) {
            return false;
        }

        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
