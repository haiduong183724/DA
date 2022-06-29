package com.example.doormanage.controller;

import com.example.doormanage.service.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/change-door-state")
public class DoorStateController {

    @Autowired
    SendMessageService sendMessageService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void ChangeDoorState(@RequestParam("state") boolean doorStateUpdate , @RequestParam("door_id") String doorId){

        if(doorStateUpdate){
            sendMessageService.SendMessageToAll("open door with id: " + doorId);
        }
        else {
            sendMessageService.SendMessageToAll("Close door with id : " + doorId);
        }
    }
}
