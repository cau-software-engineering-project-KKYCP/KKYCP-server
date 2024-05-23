package org.kkycp.server.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .formLogin(form -> form
                        .successHandler(new NullAuthenticationSuccessHandler())
                        .failureHandler(new SimpleUrlAuthenticationFailureHandler()))
                .exceptionHandling(exception -> exception.authenticationEntryPoint((request, response, authException) -> {}))
                .authorizeHttpRequests(request ->
                        request.requestMatchers("/").permitAll()
                                .requestMatchers("/signup").anonymous()
                                .anyRequest().authenticated()
                )
                .build();
    }



    //TODO: replace this with production code
    @Bean
    public UserDetailsManager userDetailsManager() {
        PasswordEncoder encoder = passwordEncoder();
        UserDetails testUser = User.withUsername("test")
                .password(encoder.encode("password"))
                .build();
        return new InMemoryUserDetailsManager(testUser);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
