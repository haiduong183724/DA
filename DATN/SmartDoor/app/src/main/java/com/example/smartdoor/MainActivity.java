package com.example.smartdoor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Debug;
import android.os.StatFs;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartdoor.R;
import com.example.smartdoor.door.DoorState;
import com.example.smartdoor.door.ListDoor;
import com.example.smartdoor.network.HttpsTrustManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import tech.gusavila92.websocketclient.WebSocketClient;

public class MainActivity extends AppCompatActivity {

    View popupView;
    List<DoorState> listDoor = new ArrayList<>();
    ListView listView;
    ListDoor listDoorAdapter;
    TextView errorMsg;
    private WebSocketClient webSocketClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        popupView = findViewById(R.id.popup_view);
        listView = findViewById(R.id.list_door);
        errorMsg = findViewById(R.id.error_message);
        GetAllDoorState(listView);
        // When the user clicks on the ListItem
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                DoorState door = (DoorState) o;
                Toast.makeText(MainActivity.this, "Selected :" + " " + door.getId(), Toast.LENGTH_LONG).show();
                ShowPopUp(popupView, door);
            }
        });
        ConnectWebSocket();
    }
    public void GetAllDoorState(ListView listView){
        String url = getResources().getString(R.string.server_host) + "/door/get-all";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Show list door on the display

                        try {
                            JSONArray arr = new JSONArray(response);
                            Log.d("RESPONSE", response);
                            for(int i = 0; i < arr.length(); i++){
                                JSONObject door = arr.getJSONObject(i);
                                String doorId = door.getString("doorId");
                                Boolean doorState = door.getBoolean("doorState");
                                String doorChanel = door.getString("doorChanel");
                                DoorState Door = new DoorState(doorId,doorState,doorChanel);
                                listDoor.add(Door);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(listDoor.size() == 0){
                            errorMsg.setText(getResources().getString(R.string.error_msg));
                        }
                        else {
                            errorMsg.setText("");
                            listDoorAdapter = new ListDoor(getApplicationContext(), listDoor);
                            listView.setAdapter(listDoorAdapter);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("REQUEST_ERR", "cant read response");
                        errorMsg.setText("Can't call API ");
                    }
                });
        queue.add(stringRequest);
    }
    public void ShowPopUp( View view, DoorState doorState){
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.door_popup, null);
        TextView openDoor = popupView.findViewById(R.id.popup_text);
        if(doorState.getState() == false){
            openDoor.setText("Open door");
        }
        else {
            openDoor.setText("Close door");
        }
        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 300);
        TextView text = popupView.findViewById(R.id.open_video);

        // dismiss the popup window when touched
        // Closes the popup window when touch outside.
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        // Removes default background.
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("POPUP" , "Close popup");
                DisplayVideo(doorState.getRtspUrl());
                popupWindow.dismiss();
                return true;
            }
        });
        openDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDoor(!doorState.getState(), doorState.getId());
                popupWindow.dismiss();
            }
        });
    }

    public void ConnectWebSocket(){
        URI uri;
        try {
            // Connect to local host
            uri = new URI(getResources().getString(R.string.web_socket) + "/user?module=android");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("WebSocket", "Session is starting");
                webSocketClient.send("Hello World!");
            }

            @Override
            public void onTextReceived(String s) {
                Log.i("WebSocket", "Message received");
                Log.d("receive message: ", s);
                String [] list = s.split(":");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Stuff that updates the UI
                        if(list.length == 2) {
                            boolean doorState = Boolean.parseBoolean(list[1].toUpperCase());
                            ChangeDoorState(list[0], doorState);
                        }
                    }
                });

            }

            @Override
            public void onBinaryReceived(byte[] data) {

            }

            @Override
            public void onPingReceived(byte[] data) {

            }

            @Override
            public void onPongReceived(byte[] data) {
            }

            @Override
            public void onException(Exception e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onCloseReceived() {
                Log.i("WebSocket", "Closed ");
                System.out.println("onCloseReceived");
            }
        };
        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

    public void ChangeDoorState(String doorId, boolean doorState){
        for (DoorState door: listDoor
             ) {
            if(door.getId().equals(doorId)){
                door.setState(doorState);
            }
        }
        listDoorAdapter.notifyDataSetChanged();
    }
    public void DisplayVideo(String rtspUrl){
        Intent intent = new Intent(this, StartStreamActivity.class);
        intent.putExtra("url", rtspUrl );
        this.startActivity(intent);
    }
    public void OpenDoor(boolean doorState, String doorId){
        String url = getResources().getString(R.string.server_host) + "/door/change-door-state?" +"door_id="+doorId +"&state="+String.valueOf(doorState) ;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Show list door on the display

                        try {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            Log.d("RESPONSE", response);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("REQUEST_ERR", "cant read response");
                        errorMsg.setText("Can't call API ");
                    }
                });
        queue.add(stringRequest);
    }

}