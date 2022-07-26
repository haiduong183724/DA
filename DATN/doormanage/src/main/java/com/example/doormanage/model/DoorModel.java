package com.example.doormanage.model;

import com.example.doormanage.entity.Door;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class DoorModel {

    public static final String doorDataPath = "Door.json";
    public DoorModel(){
    }
    public void addDoor(Door door){

    }
    public void updateDoorState(){

    }
    public static List<Door> getAllDoor(){
        try {
            File file = ResourceUtils.getFile("classpath:Door.json");
            JsonReader reader = new JsonReader(new FileReader(file));
            Gson gson = new Gson();
            List<Door> listDoor = Arrays.asList(gson.fromJson(reader, Door[].class));
            return listDoor;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void writeJSONtoFile(ArrayList<Object>list){

    }

}
