package org.kkycp.server.auth.test;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.auth.NullAuthenticationSuccessHandler;
import org.kkycp.server.auth.SecurityConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class TestSecurityConfiguration {
    @Bean
    @Profile("!prod")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        SecurityConfig.applySecurityConfiguration(http);
        return http.build();
    }
}
