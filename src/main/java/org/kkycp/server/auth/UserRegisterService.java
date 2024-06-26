package org.kkycp.server.auth;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.auth.jpa.AuthUserDetails;
import org.kkycp.server.repo.UserRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserRegisterService {
    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;

    public void signUp(AuthUserDetails authUser) {
        if (userDetailsManager.userExists(authUser.getUsername())) {
            throw new RegisterController.InvalidRegistrationException("This username is already in use.");
        }

        createUser(authUser);
    }

    private void createUser(UserDetails authUser) {
        userDetailsManager.createUser(authUser);
        org.kkycp.server.domain.User domainUser = new org.kkycp.server.domain.User(authUser.getUsername());
        userRepo.save(domainUser);
    }

    public void signUp(RegisterDto.Request request) {
        if (userDetailsManager.userExists(request.getUsername())) {
            throw new RegisterController.InvalidRegistrationException("This username is already in use.");
        }

        UserDetails newUserDetails = User.withUsername(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        createUser(newUserDetails);
    }

    public boolean isUserExists(String username) {
        return userDetailsManager.userExists(username);
    }
}
