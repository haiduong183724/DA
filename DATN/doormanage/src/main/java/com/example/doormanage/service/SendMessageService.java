package com.example.doormanage.service;

import com.example.doormanage.manage.SessionManage;
import org.springframework.stereotype.Service;

import javax.websocket.Session;

@Service
public class SendMessageService {
    public void SendMessageToAll(String message){
        SessionManage.sendAll(message);
    }

}
