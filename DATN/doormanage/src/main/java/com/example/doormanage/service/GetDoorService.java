package com.example.doormanage.service;

import com.example.doormanage.entity.Door;
import com.example.doormanage.manage.SessionManage;
import com.example.doormanage.model.DoorModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class GetDoorService {
    public  static ArrayList<Door> listDoor;
    Lock lock = new ReentrantLock();
    public GetDoorService(){
        listDoor = new ArrayList<Door>(DoorModel.getAllDoor());
    }
    public List<Door> GetAllDoor(){
        if(listDoor.isEmpty()){
            return null;
        }
        return listDoor;
    }

    public String addDoor(String DoorId, String doorUrl){
        boolean isIdExist = false;
        for(Door door : listDoor){
            if(door.getDoorId().equals((DoorId))){
                return "The door has been added in the list door";
            }
        }
        Door door = new Door(DoorId, false, doorUrl);
        lock.lock();
        listDoor.add(door);
        lock.unlock();
        return "the door has been added successfully";
    }

    public String removeDoor(String DoorId){
        boolean isIdExist = false;
        int index = -1;
        for(Door door : listDoor){
            if(door.getDoorId().equals((DoorId))){
                index = listDoor.lastIndexOf(door);
                return "The door has been added in the list door";
            }
        }
        if(index >= 0){
            lock.lock();
            listDoor.remove(index);
            lock.unlock();
            return "the door has been removed successfully";
        }
        else {
            return "the door didn't exist";
        }
    }
    public String ChangeDoorState(String doorId, boolean doorState){
        // Check the doorId exist
        boolean isIdExist = false;
        for(Door door : listDoor){
            if(door.getDoorId().equals((doorId))){
                isIdExist = true;
                // Check the door state
                if(door.isDoorState() == doorState){
                    if(doorState == true){
                        return "The door has been already opened";
                    }
                    else {
                        return "The door has been already closed";
                    }
                }
                else {
                    // update the door state
                    door.setDoorState(doorState);
                    SessionManage.sendAll(doorId+":"+doorState);
                    return "Change door state success";
                }
            }
        }
        if(isIdExist = false){
            return "The door Id doesn't exist";
        }

        return null;
    }


}
