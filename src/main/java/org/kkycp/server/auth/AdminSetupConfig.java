package org.kkycp.server.auth;

import org.kkycp.server.auth.jpa.AuthGrantedAuthority;
import org.kkycp.server.auth.jpa.AuthUserDetails;
import org.kkycp.server.auth.jpa.JpaUserDetailsManager;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.Collections;

@Configuration
public class AdminSetupConfig {
    private final JpaUserDetailsManager jpaUserDetailsManager;

    public AdminSetupConfig(JpaUserDetailsManager jpaUserDetailsManager) {
        this.jpaUserDetailsManager = jpaUserDetailsManager;
    }

    @EventListener(ApplicationReadyEvent.class)
    void readyAdmin() {
        AuthUserDetails authUserDetails = new AuthUserDetails("admin", "admin");
        AuthGrantedAuthority adminRole = new AuthGrantedAuthority("ROLE_ADMIN");
        authUserDetails.addAuthorities(Collections.singleton(adminRole));
        jpaUserDetailsManager.createUser(authUserDetails);
    }
}
