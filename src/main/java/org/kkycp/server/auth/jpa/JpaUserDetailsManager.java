package org.kkycp.server.auth.jpa;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaUserDetailsManager implements UserDetailsManager {

    private final AuthUserDetailsRepo repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("No user found with username = " + username));
    }

    @Override
    public void createUser(UserDetails user) {
        AuthUserDetails authUser;
        if (user instanceof User securityUser) {
            authUser = AuthUserDetails.builder()
                    .username(securityUser.getUsername())
                    .password(securityUser.getPassword())
                    .build();
            authUser.addAuthorities(securityUser.getAuthorities()
                    .stream()
                    .map(auth -> new AuthGrantedAuthority(auth.getAuthority()))
                    .toList());
        } else {
            authUser = (AuthUserDetails) user;
        }

        repository.save(authUser);
    }

    @Override
    public void updateUser(UserDetails user) {
        repository.save((AuthUserDetails) user);
    }

    @Override
    public void deleteUser(String username) {
        AuthUserDetails userDetails = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No User found for username -> " + username));
        repository.delete(userDetails);
    }

    /**
     * This method assumes that both oldPassword and the newPassword params
     * are encoded with configured passwordEncoder
     *
     * @param oldPassword the old password of the user
     * @param newPassword the new password of the user
     */
    @Override
    @Transactional
    public void changePassword(String oldPassword, String newPassword) {
        AuthUserDetails userDetails = repository.findByPassword(oldPassword)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid password "));
        userDetails.setPassword(newPassword);
        repository.save(userDetails);
    }

    @Override
    public boolean userExists(String username) {
        return repository.findByUsername(username).isPresent();
    }
}
