package com.example.doormanage.controller;

import com.example.doormanage.entity.Door;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.doormanage.service.GetDoorService;

import java.util.List;
@RestController
@RequestMapping("/door")
public class DoorController {


    @Autowired
    GetDoorService doorService;
    @GetMapping("/get-all")
    public List<Door> GetListDoor(){
        return this.doorService.GetAllDoor();
    }
    @PostMapping("/change-door-state")
    public String ChangeDoorState(@RequestParam("door_id") String doorId, @RequestParam("state") boolean state){
        return this.doorService.ChangeDoorState(doorId, state);
    }
    @PostMapping("/add-door")
    public String AddDoor(@RequestBody() Door door){
        return  this.doorService.addDoor(door.getDoorId(), door.getDoorChanel());
    }
}
