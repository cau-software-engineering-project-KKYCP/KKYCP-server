package org.kkycp.server.controller.admin;


import lombok.Data;
import org.kkycp.server.domain.authorization.Privilege;

import java.util.Collection;
import java.util.List;

import static java.util.Comparator.naturalOrder;

public class UserPrivilegeDto {

    /**
     * for json such as
     * [
     *  {
     *      "username": "user1",
     *      "privileges": ["PARTICIPANT", "TRIAGER"]
     *  },
     *  {
     *      "username": "user2",
     *      "privileges": ["PARTICIPANT", "TESTER"]
     *  }
     * ]
     */
    @Data
    public static class Response {
        String username;
        List<Privilege> privileges;

        public Response(String username, Collection<Privilege> privileges) {
            this.username = username;
            this.privileges = privileges.stream().sorted(naturalOrder()).toList();
        }
    }
}
