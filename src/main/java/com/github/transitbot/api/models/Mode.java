package com.github.transitbot.api.models;

public enum Mode {
    DEPART_BEFORE("depart-before"),
    DEPART_AFTER("depart-after"),
    ARRIVE_BEFORE("arrive-before"),
    ARRIVE_AFTER("arrive-after");

    private final String name;

    Mode(String name) {
        this.name = name;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
