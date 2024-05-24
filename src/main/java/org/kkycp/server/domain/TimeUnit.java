package org.kkycp.server.domain;

import lombok.Getter;

public enum TimeUnit {
    YEAR("YEAR"),
    MONTH("MONTH"),
    WEEK("WEEK"),
    DAY("DAY");

    @Getter
    private final String value;

    TimeUnit(String value) {
        this.value = value;
    }
}
