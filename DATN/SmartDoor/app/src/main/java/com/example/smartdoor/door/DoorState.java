package com.example.smartdoor.door;

public class DoorState {


    private String doorId;
    private Boolean doorState;
    private String doorChanel;
    public DoorState(String id, boolean isOpen, String rtspUrl){
        this.doorId = id;
        this.doorState = isOpen;
        this.doorChanel = rtspUrl;
    }
    public String getId() {
        return doorId;
    }

    public void setId(String id) {
        this.doorId = id;
    }

    public Boolean getState() {
        return doorState;
    }

    public void setState(Boolean state) {
        this.doorState = state;
    }

    public String getRtspUrl() {
        return doorChanel;
    }

    public void setRtspUrl(String rtspUrl) {
        this.doorChanel = rtspUrl;
    }
}
