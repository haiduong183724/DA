package com.example.doormanage.entity;

public interface ISession {

    String getId();


    boolean send(Object data);

    void setLastTime(long lastTime);

    long getLastTime();

    void disconnect();

    void disconnect(Integer code, String message);

    void close();

    void close(Integer code, String message);

    boolean isOpen();

}
