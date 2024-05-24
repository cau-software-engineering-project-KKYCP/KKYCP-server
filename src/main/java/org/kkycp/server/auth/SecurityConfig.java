package org.kkycp.server.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    @Profile("prod")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        applySecurityConfiguration(http);
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(
                (request, response, authException) -> {
                }
        ));
        return http.build();
    }

    public static void applySecurityConfiguration(HttpSecurity http) throws Exception {
        http.formLogin(form -> form.successHandler(new NullAuthenticationSuccessHandler())
                        .failureHandler(new SimpleUrlAuthenticationFailureHandler()))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsSource()))
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))

                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers("/", "/index.html", "/signup", "/login").permitAll()
                                .anyRequest().authenticated());
    }

    public static CorsConfigurationSource corsSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.setAllowCredentials(true);
        config.addAllowedHeader("X-Requested-With");
        config.addAllowedHeader("Content-Type");
        config.addAllowedMethod(HttpMethod.POST);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/logout", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
