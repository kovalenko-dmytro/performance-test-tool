package com.gmail.apachdima.ptt.common.constant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Model {

    USER("User"),
    ROLE("Role"),
    STORED_FILE("File"),
    SIMULATION("Simulation"),
    TEST_EXECUTION("Test execution");

    private final String name;

    @AllArgsConstructor
    @Getter
    public enum Field {

        ID("id"),
        USER_NAME("username"),
        NAME("name");

        private final String fieldName;
    }
}
