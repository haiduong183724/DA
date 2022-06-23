package com.example.doormanage.controller;

import com.example.doormanage.entity.Door;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
