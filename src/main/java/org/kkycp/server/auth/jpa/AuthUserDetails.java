package org.kkycp.server.auth.jpa;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "auth_user",
        uniqueConstraints = @UniqueConstraint(columnNames = "username"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthUserDetails implements UserDetails, Serializable {
    @Serial
    private static final long serialVersionUID = 42L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @Setter
    private String password;
    private final boolean accountNonExpired = true;
    private final boolean accountNonLocked = true;
    private final boolean credentialsNonExpired = true;
    private final boolean enabled = true;

    @OneToMany(mappedBy = "authUserDetails", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<AuthGrantedAuthority> authorities = new HashSet<>();

    @Builder
    public AuthUserDetails(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void addAuthorities(Collection<AuthGrantedAuthority> authorities) {
        authorities.forEach(authority -> authority.setAuthUserDetails(this));
        this.authorities.addAll(authorities);
    }
}