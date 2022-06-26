package com.example.doormanage.controller;

import com.example.doormanage.service.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/change-door-state")
public class DoorStateController {

    @Autowired
    SendMessageService sendMessageService;

    @RequestMapping("")
    public void ChangeDoorState(@RequestParam("state") boolean doorStateUpdate){

        if(doorStateUpdate){
            sendMessageService.SendMessageToAll("open door");
        }
        else {
            sendMessageService.SendMessageToAll("Close door");
        }
    }
}
