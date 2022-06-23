package com.example.doormanage.service;

import com.example.doormanage.entity.Door;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetDoorService {

    public List<Door> GetAllDoor(){
        List<Door> listDoor = new ArrayList<Door>();
        Door d1 = new Door("1" , true, "rtsp://localhost:5555");
        Door d2 = new Door("2" , true, "rtsp://localhost:5555");
        Door d3 = new Door("3" , true, "rtsp://localhost:5555");
        listDoor.add(d1);
        listDoor.add(d2);
        listDoor.add(d3);
        return  listDoor;
    }

}
