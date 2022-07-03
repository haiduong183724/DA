package com.example.doormanage.model;

import com.example.doormanage.entity.Door;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.http.converter.json.GsonBuilderUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
public class DoorModel {

    public final String doorDataPath = "";
    public DoorModel(){
    }
    public void addDoor(Door door){
        try {
            JSONParser parser = new JSONParser(new FileReader(doorDataPath));
            ArrayList<Object> list = parser.parseArray();
            list.add(door);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateDoorState(){

    }
    public List<Door> getAllDoor(){
        List<Door> listDoor = new ArrayList<>();

        return  listDoor;
    }
    public void writeJSONtoFile(ArrayList<Object>list){

    }

}
