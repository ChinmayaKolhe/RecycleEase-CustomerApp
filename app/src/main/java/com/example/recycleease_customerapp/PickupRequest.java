package com.example.recycleease_customerapp;

public class PickupRequest {
    private String id;
    private String name;
    private String phone;
    private String wasteType;
    private String pickupDate;
    private String address;
    private String status;
    private String location;
    private String photoUrl;

    // Default constructor required for Firebase
    public PickupRequest() {}

    public PickupRequest(String id, String name, String phone, String wasteType, String pickupDate, String address, String status, String location, String photoUrl) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.wasteType = wasteType;
        this.pickupDate = pickupDate;
        this.address = address;
        this.status = status;
        this.location = location;
        this.photoUrl = photoUrl;
    }

    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getWasteType() { return wasteType; }
    public String getPickupDate() { return pickupDate; }
    public String getAddress() { return address; }
    public String getStatus() { return status; }
    public String getLocation() { return location; }
    public String getPhotoUrl() { return photoUrl; }
    public void setStatus(String status) { this.status = status; }
    public void setLocation(String location) { this.location = location; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
}
