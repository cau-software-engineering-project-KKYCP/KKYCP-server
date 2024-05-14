package org.kkycp.server.domain;

import java.util.Map;

public class Project {
    private int id;
    private String projectName;
    private Map<User, Participation> participationByUser;
}
