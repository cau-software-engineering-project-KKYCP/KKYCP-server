package org.kkycp.server.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Project {
    @Id
    @GeneratedValue
    private int id;

    private String projectName;

}
