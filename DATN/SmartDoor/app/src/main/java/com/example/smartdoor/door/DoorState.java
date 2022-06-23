package com.example.smartdoor.door;

public class DoorState {


    private String id;
    private Boolean state;
    private String rtspUrl;
    public DoorState(String id, boolean isOpen, String rtspUrl){
        this.id = id;
        this.state = isOpen;
        this.rtspUrl = rtspUrl;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getRtspUrl() {
        return rtspUrl;
    }

    public void setRtspUrl(String rtspUrl) {
        this.rtspUrl = rtspUrl;
    }
}
