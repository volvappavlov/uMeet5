package com.budek.umeet5;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.budek.umeet5.adapter.CustomListViewAdapter;
import com.budek.umeet5.objects.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    protected Button addEvent;
    protected ListView upcomingEvents, pastEvents;
    protected CustomListViewAdapter upcomingEventsAdapter, pastEventsAdapter;
    protected ArrayList<Event> upcomingEventList = new ArrayList<>();
    protected ArrayList<Event> pastEventList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeButtons();
        initializeListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_disconnect:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeButtons(){
        addEvent = (Button)findViewById(R.id.add_event);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddEventActivity.class));
            }
        });
    }


    private void initializeListView(){
        upcomingEvents = (ListView) findViewById(R.id.upcoming_events);
        pastEvents = (ListView) findViewById(R.id.past_events);
        upcomingEventsAdapter = new CustomListViewAdapter(this, upcomingEventList);
        upcomingEvents.setAdapter(upcomingEventsAdapter);
        upcomingEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent eventPreview = new Intent(MainActivity.this, EventPreviewActivity.class);
                eventPreview.putExtra("eventName", upcomingEventList.get(i).getName());
                eventPreview.putExtra("date", upcomingEventList.get(i).getDateTimestamp());
                eventPreview.putExtra("username", upcomingEventList.get(i).getUser());
                eventPreview.putExtra("latitude", upcomingEventList.get(i).getLatitude());
                eventPreview.putExtra("longitude", upcomingEventList.get(i).getLongitude());
                startActivity(eventPreview);
            }
        });


        pastEventsAdapter = new CustomListViewAdapter(this, pastEventList);

        pastEvents.setAdapter(pastEventsAdapter);

        pastEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent eventPreview = new Intent(MainActivity.this, EventPreviewActivity.class);
                eventPreview.putExtra("eventName", pastEventList.get(i).getName());
                eventPreview.putExtra("date", pastEventList.get(i).getDateTimestamp());
                eventPreview.putExtra("username", pastEventList.get(i).getUser());
                eventPreview.putExtra("latitude", pastEventList.get(i).getLatitude());
                eventPreview.putExtra("longitude", pastEventList.get(i).getLongitude());
                startActivity(eventPreview);
            }
        });

        prepareEventsDatas();
    }


    private void prepareEventsDatas(){
        FirebaseDatabase.getInstance("https://umeet5-default-rtdb.firebaseio.com").getReference().child("events").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Event event = dataSnapshot.getValue(Event.class);
                if(event.getDateTimestamp() * 1000L < System.currentTimeMillis()){
                    pastEventList.add(event);
                    pastEventsAdapter.notifyDataSetChanged();
                } else {
                    upcomingEventList.add(event);
                    upcomingEventsAdapter.notifyDataSetChanged();
                }

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

}
