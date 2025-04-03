package com.example.recycleease_customerapp;
import java.util.List;

public class RecyclingCenter {
    private String id;
    private String name;
    private String address;
    private String contact;
    private List<String> wasteTypes;
    private String workingHours;
    private String description;

    public RecyclingCenter() {}

    public RecyclingCenter(String id, String name, String address, String contact, List<String> wasteTypes, String workingHours, String description) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.wasteTypes = wasteTypes;
        this.workingHours = workingHours;
        this.description = description;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getContact() { return contact; }
    public List<String> getWasteTypes() { return wasteTypes; }
    public String getWorkingHours() { return workingHours; }
    public String getDescription() { return description; }
}

