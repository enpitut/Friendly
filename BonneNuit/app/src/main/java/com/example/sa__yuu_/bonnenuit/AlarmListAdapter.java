package com.example.sa__yuu_.bonnenuit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AlarmListAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<AlarmStatus> alarmList;

    public AlarmListAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setAlarmList(ArrayList<AlarmStatus> alarmList) {
        this.alarmList = alarmList;
    }

    @Override
    public int getCount() {
        return alarmList.size();
    }

    @Override
    public Object getItem(int position) {
        return alarmList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return alarmList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String[] day = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        convertView = layoutInflater.inflate(R.layout.alarm_status,parent,false);
        AlarmStatus status = alarmList.get(position);
        ((TextView)convertView.findViewById(R.id.day_of_week)).setText(day[status.getDayOfWeek()]);
        if (status.getDayOfWeek()==0) {
            ((TextView)convertView.findViewById(R.id.day_of_week)).setTextColor(0xffff0000);
        } else if (status.getDayOfWeek()==6) {
            ((TextView)convertView.findViewById(R.id.day_of_week)).setTextColor(0xff0000ff);
        }
        ((TextView)convertView.findViewById(R.id.alarm_time)).setText(status.getAlarmTime());
        ((TextView)convertView.findViewById(R.id.alarm_status)).setText(status.getEnable() ? "ON" : "OFF");

        return convertView;
    }
}