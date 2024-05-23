package org.kkycp.server.domain;

import java.time.LocalDate;

public class Filter {

    public int filtercase;

    public LocalDate from;
    public LocalDate to;

    public Issue.Status status;

    public Issue.Priority priority;

    public Filter(LocalDate from, LocalDate to){
        filtercase=1;
        this.from=from;
        this.to=to;
    }
    public Filter(Issue.Status status){
        filtercase=2;
        this.status=status;
    }

    public Filter(Issue.Priority priority){
        filtercase=3;
        this.priority=priority;
    }
}
