package com.scriptobotic.api;

public enum ControllerState {
    INIT("Initialized"),
    STARTED("Started"),
    STOPPED("Stopped"),
    JOINED("Joined");

    private final String desc;

    ControllerState(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }
}
