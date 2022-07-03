package com.example.doormanage.entity;

import org.springframework.stereotype.Component;

import java.util.UUID;

public class Door {
    private UUID doorId;
    private String doorName;
    private boolean doorState;
    private String doorChanel;
    public Door(){

    }
    public Door(String doorId, boolean doorState, String doorChanel) {
        this.doorId = UUID.randomUUID();
        this.doorName = doorId;
        this.doorState = doorState;
        this.doorChanel = doorChanel;
    }
    public Door(String doorId, String doorChanel) {
        this.doorName = doorId;
        this.doorState = false;
        this.doorChanel = doorChanel;
    }

    public String getDoorId() {
        return doorName;
    }

    public void setDoorId(String doorId) {
        this.doorName = doorId;
    }

    public boolean isDoorState() {
        return doorState;
    }

    public void setDoorState(boolean doorState) {
        this.doorState = doorState;
    }

    public String getDoorChanel() {
        return doorChanel;
    }

    public void setDoorChanel(String doorChanel) {
        this.doorChanel = doorChanel;
    }
}
