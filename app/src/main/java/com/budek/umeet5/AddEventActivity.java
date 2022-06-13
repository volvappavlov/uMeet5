package com.budek.umeet5;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.budek.umeet5.objects.Event;

import java.util.Arrays;
import java.util.Calendar;



public class AddEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "AutoComplete";

    private Calendar savedCalendar;

    private DatabaseReference mDatabase;


    protected Button addEvent;
    protected EditText eventName;
    protected ImageButton editDate, editTime;
    protected TextView previewDate, previewTime;


    private Event eventToSave = new Event();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event2);
        savedCalendar = Calendar.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://umeet5-default-rtdb.firebaseio.com").getReference().child("events");
        initializeButtons();
        initializeEditText();
        initializeAutoCompleteFragment();
        initializeTextViews();
        initializeImageButtons();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        savedCalendar.set(Calendar.YEAR, year);
        savedCalendar.set(Calendar.MONTH, monthOfYear);
        savedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        previewDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
        savedCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
        savedCalendar.set(Calendar.MINUTE, selectedMinute);
        previewTime.setText(selectedHour + ":" + selectedMinute);
    }

    private void initializeButtons(){
        addEvent = (Button)findViewById(R.id.button_validate);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventToSave.setName(eventName.getText().toString());
                eventToSave.setUser(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                eventToSave.setDateTimestamp(savedCalendar.getTimeInMillis() / 1000L);
                mDatabase.child(String.valueOf(savedCalendar.getTimeInMillis() / 1000L)).setValue(eventToSave, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        startActivity(new Intent(AddEventActivity.this, MainActivity.class));
                    }
                });
            }
        });
    }

    private void initializeEditText(){
        eventName = (EditText)findViewById(R.id.event_name);
    }

    private void initializeAutoCompleteFragment(){
        Places.initialize(getApplicationContext(), "AIzaSyCMWCMclUCpmiA9vp2g6td8lJ66hfjFcR0");
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                eventToSave.setLatitude(place.getLatLng().latitude);
                eventToSave.setLongitude(place.getLatLng().longitude);
            }


            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }


    private void initializeTextViews(){
        previewDate = (TextView)findViewById(R.id.event_date_preview);
        previewTime = (TextView)findViewById(R.id.event_time_preview);
    }


    private void initializeImageButtons(){
        editDate = (ImageButton)findViewById(R.id.event_date_button);
        editTime = (ImageButton)findViewById(R.id.event_time_button);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(AddEventActivity.this, AddEventActivity.this, mYear, mMonth, mDay).show();
            }
        });


        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mCurrentTime = Calendar.getInstance();
                int mHour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int mMinute = mCurrentTime.get(Calendar.MINUTE);
                new TimePickerDialog(AddEventActivity.this, AddEventActivity.this, mHour, mMinute, true).show();
            }
        });
    }



}