package org.kkycp.server.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.springframework.security.core.userdetails.User.withUsername;

public class AuthenticationUtil {
    public static void insertTestUser() {
        SecurityContextHolder.getContext().setAuthentication(
                UsernamePasswordAuthenticationToken.authenticated(withUsername("tester").password("password").build(), null, null));
    }
}
