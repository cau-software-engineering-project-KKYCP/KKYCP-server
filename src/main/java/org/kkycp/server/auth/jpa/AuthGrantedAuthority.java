package org.kkycp.server.auth.jpa;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthGrantedAuthority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String authority;

    @ManyToOne
    @JoinColumn(name = "auth_user_detail_id")
    @Setter
    private AuthUserDetails authUserDetails;

    public AuthGrantedAuthority(String authority) {
        this.authority = authority;
    }
}
