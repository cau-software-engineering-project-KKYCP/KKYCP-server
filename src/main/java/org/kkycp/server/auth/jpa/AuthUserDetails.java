package org.kkycp.server.auth.jpa;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthUserDetails implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @Setter
    private String password;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

    @OneToMany(mappedBy = "authUserDetails", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AuthGrantedAuthority> authorities = new HashSet<>();

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