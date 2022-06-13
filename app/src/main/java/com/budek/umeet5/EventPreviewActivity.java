package com.budek.umeet5;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class EventPreviewActivity extends AppCompatActivity implements OnMapReadyCallback {

    protected GoogleMap mMap;
    protected TextView eventName, userName, eventDate;
    protected Button comeToEvent, notComeToEvent;
    protected ListView participantListView;
    protected ArrayAdapter<String> participantArrayAdapter;
    private ArrayList<String> participantArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_preview);
        initializeMap();
        initializeTextViews();
        initializeListView();
        initializeButton();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(getIntent().getDoubleExtra("latitude", -34), getIntent().getDoubleExtra("longitude", 151));
        mMap.addMarker(new MarkerOptions().position(sydney).title(getIntent().getStringExtra("eventName")));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    private void initializeMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private void initializeTextViews(){

        eventName = (TextView)findViewById(R.id.event_name);
        userName = (TextView)findViewById(R.id.event_username);
        eventDate = (TextView)findViewById(R.id.event_date);
        eventName.setText(getIntent().getStringExtra("eventName"));
        userName.setText(getIntent().getStringExtra("username"));
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTimeInMillis(getIntent().getLongExtra("dateTimestamp", 0));
        eventDate.setText(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault()).format(new Date(getIntent().getLongExtra("date", 0) * 1000L)));

    }


    private void initializeListView(){

        participantListView = (ListView)findViewById(R.id.participant_list);
        participantArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, participantArrayList);
        participantListView.setAdapter(participantArrayAdapter);
        prepareParticipantDatas();
    }


    private void initializeButton(){
        comeToEvent = (Button)findViewById(R.id.come_to_event);
        notComeToEvent = (Button)findViewById(R.id.not_come_to_event);
        comeToEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance("https://umeet5-default-rtdb.firebaseio.com").getReference().child("events").child(String.valueOf(getIntent().getLongExtra("date", 0))).child("participant").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).setValue(1);
            }
        });
        notComeToEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance("https://umeet5-default-rtdb.firebaseio.com").getReference().child("events").child(String.valueOf(getIntent().getLongExtra("date", 0))).child("participant").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).setValue(0);
            }
        });
        if(getIntent().getLongExtra("date", 0) * 1000L < System.currentTimeMillis()){
            comeToEvent.setVisibility(View.GONE);
            notComeToEvent.setVisibility(View.GONE);
        }

    }


    private void prepareParticipantDatas(){
        FirebaseDatabase.getInstance("https://umeet5-default-rtdb.firebaseio.com").getReference().child("events").child(String.valueOf(getIntent().getLongExtra("date", 0))).child("participant").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.getValue().toString().equals(String.valueOf(1))){
                        participantArrayList.add(dataSnapshot.getKey());
                        participantArrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getValue().toString().equals(String.valueOf(0))){
                    participantArrayList.remove(dataSnapshot.getKey());
                    participantArrayAdapter.notifyDataSetChanged();
                } else if(dataSnapshot.getValue().toString().equals(String.valueOf(1))){
                    participantArrayList.add(dataSnapshot.getKey());
                    participantArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }


}
