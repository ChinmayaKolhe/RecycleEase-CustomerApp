package com.example.recycleease_customerapp;

public class  RecyclingCenter {
    private String name;
    private String location;
    private String contact;

    // Default constructor (needed for Firebase)
    public RecyclingCenter() {}

    // Parameterized constructor
    public RecyclingCenter(String name, String location, String contact) {
        this.name = name;
        this.location = location;
        this.contact = contact;
    }

    // Getters with Null Safety
    public String getName() { return name != null ? name : "No Name"; }

    public String getLocation() { return location != null ? location : "No Address"; }

    public String getContact() { return contact != null ? contact : "No Contact Info"; }
}
