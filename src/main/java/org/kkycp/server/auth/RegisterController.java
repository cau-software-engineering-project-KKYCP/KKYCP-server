package org.kkycp.server.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RegisterController {
    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@ModelAttribute RegisterDto.Request request) {
        if (userDetailsManager.userExists(request.getUsername())) {
            throw new InvalidRegistrationException("This username is already in use.");
        }

        UserDetails newUserDetails = User.withUsername(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userDetailsManager.createUser(newUserDetails);
    }

    @ExceptionHandler(InvalidRegistrationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleInvalidRegistration(InvalidRegistrationException exception) {
        return Map.of("error", exception.getMessage());
    }


    public static class InvalidRegistrationException extends RuntimeException {
        public InvalidRegistrationException(String message) {
            super(message);
        }
    }
}

