package com.example.smartdoor.door;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.smartdoor.R;
import com.example.smartdoor.door.DoorState;

import java.util.List;

public class ListDoor extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<DoorState> listDoors;
    public ListDoor(Context context, List<DoorState> list){
        this.context = context;
        this.listDoors = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listDoors.size();
    }

    @Override
    public Object getItem(int position) {
        return  this.listDoors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_layout, null);
            holder = new ViewHolder();
            holder.doorNameView = (TextView) convertView.findViewById(R.id.door_name);
            holder.doorStateView = (TextView) convertView.findViewById(R.id.door_state);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DoorState door = this.listDoors.get(position);
        holder.doorNameView.setText(door.getId());
        holder.doorStateView.setText(door.getState()? "Open" : "Close");
        return convertView;
    }
    static class ViewHolder {
        TextView doorNameView;
        TextView doorStateView;
    }

}
