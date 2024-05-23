package org.kkycp.server.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class TestConfiguration {
    private final UserRegisterService userRegisterService;

    @EventListener(ApplicationReadyEvent.class)
    public void setupTestUser() {
        RegisterDto.Request testUserRequest = new RegisterDto.Request("test", "password");
        userRegisterService.signUp(testUserRequest);
    }
}
