package com.budek.umeet5.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.budek.umeet5.R;
import com.budek.umeet5.objects.Event;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;




public class CustomListViewAdapter extends ArrayAdapter<Event> {

    public CustomListViewAdapter(Context context, ArrayList<Event> eventArrayList) {
        super(context, 0, eventArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Event event = getItem(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.events_list_row, parent, false);
        }


        TextView eventName = (TextView) convertView.findViewById(R.id.eventName);
        TextView dateName = (TextView) convertView.findViewById(R.id.eventDate) ;


        eventName.setText(event.getName());
        dateName.setText(getDate(event.getDateTimestamp()));


        return convertView;
    }

    private String getDate(long date){
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault()).format(date * 1000L);
    }



}
