package com.example.doormanage.entity;

import org.springframework.stereotype.Component;

public class Door {
    private String doorId;
    private boolean doorState;
    private String doorChanel;
    public Door(){

    }
    public Door(String doorId, boolean doorState, String doorChanel) {
        this.doorId = doorId;
        this.doorState = doorState;
        this.doorChanel = doorChanel;
    }

    public String getDoorId() {
        return doorId;
    }

    public void setDoorId(String doorId) {
        this.doorId = doorId;
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
