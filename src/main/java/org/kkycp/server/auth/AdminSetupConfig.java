package org.kkycp.server.auth;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.auth.jpa.AuthGrantedAuthority;
import org.kkycp.server.auth.jpa.AuthUserDetails;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class AdminSetupConfig {
    private final UserRegisterService registerService;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    void readyAdmin() {
        String adminName = "admin";
        if (registerService.isUserExists(adminName)) {
            return;
        }

        AuthUserDetails admin = new AuthUserDetails(adminName, passwordEncoder.encode("admin"));
        AuthGrantedAuthority adminRole = new AuthGrantedAuthority("ROLE_ADMIN");
        admin.addAuthorities(Collections.singleton(adminRole));

        registerService.signUp(admin);
    }
}
