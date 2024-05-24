package org.kkycp.server.auth.test;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.auth.NullAuthenticationSuccessHandler;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsSource) throws Exception {
        return http
                .formLogin(form -> form
                        .successHandler(new NullAuthenticationSuccessHandler())
                        .failureHandler(new SimpleUrlAuthenticationFailureHandler()))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsSource))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                //.exceptionHandling(exception -> exception.authenticationEntryPoint((request, response, authException) -> {}))
                .authorizeHttpRequests(request ->
                        request.requestMatchers("/", "/index.html", "/signup", "/login").permitAll()
                                .anyRequest().authenticated()
                )
                .build();
    }
}
