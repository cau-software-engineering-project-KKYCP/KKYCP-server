package org.kkycp.server.auth;

import org.junit.jupiter.api.Test;
import org.kkycp.server.auth.authorizaiton.PrivilegeChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
public class AuthorizationTest {
    @Autowired
    @Qualifier("authz")
    private PrivilegeChecker privilegeChecker;


    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void checkPrivilege() {

    }

    @PreAuthorize("@authz.hasPrivilege(#root, 1L, T(org.kkycp.server.domain.authorization.Privilege).PARTICIPANT)")
    private void testAuthMethod() {}

}
