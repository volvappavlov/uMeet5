package com.budek.umeet5.objects;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;



@IgnoreExtraProperties
public class Event {

    private String name;
    private String user;
    private long dateTimestamp;
    private double latitude;
    private double longitude;


    public Event(){ }


    public Event(String name, String username, long dateTimestamp, double latitude, double longitude){
        this.name = name;
        this.user = username;
        this.dateTimestamp = dateTimestamp;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName(){ return this.name; }
    public String getUser(){ return this.user; }
    public long getDateTimestamp(){ return this.dateTimestamp; }
    public double getLatitude(){ return this.latitude; }
    public double getLongitude(){ return this.longitude; }

    public void setName(String name){ this.name = name; }
    public void setUser(String user){ this.user = user; }
    public void setDateTimestamp(long dateTimestamp){ this.dateTimestamp = dateTimestamp; }
    public void setLatitude(double latitude){ this.latitude = latitude; }
    public void setLongitude(double longitude){ this.longitude = longitude; }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", user='" + user + '\'' +
                ", dateTimestamp=" + dateTimestamp +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("user", user);
        result.put("dateTimestamp", dateTimestamp);
        result.put("latitude", latitude);
        result.put("longitude", longitude);

        return result;
    }
}
