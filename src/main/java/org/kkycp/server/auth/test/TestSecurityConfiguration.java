package org.kkycp.server.auth.test;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.auth.SecurityConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

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
