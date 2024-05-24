package org.kkycp.server.auth.test;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.auth.RegisterDto;
import org.kkycp.server.auth.UserRegisterService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("!prod")
public class TestSecuritySetup {
    private final UserRegisterService userRegisterService;

    @EventListener(ApplicationReadyEvent.class)
    public void setupTestUser() {
        RegisterDto.Request testUserRequest = new RegisterDto.Request("test", "password");
        userRegisterService.signUp(testUserRequest);
    }
}
