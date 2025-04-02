package com.example.recycleease_customerapp;

public class DonationRequest {
    private String id;
    private String donorName;
    private String donorPhone;
    private String itemType;
    private String address;
    private String pickupDate;
    private String status;

    // Default constructor for Firebase
    public DonationRequest() {}

    public DonationRequest(String id, String donorName, String donorPhone, String itemType, String address, String pickupDate, String status) {
        this.id = id;
        this.donorName = donorName;
        this.donorPhone = donorPhone;
        this.itemType = itemType;
        this.address = address;
        this.pickupDate = pickupDate;
        this.status = status;
    }

    // Getters
    public String getId() { return id; }
    public String getDonorName() { return donorName; }
    public String getDonorPhone() { return donorPhone; }
    public String getItemType() { return itemType; }
    public String getAddress() { return address; }
    public String getPickupDate() { return pickupDate; }
    public String getStatus() { return status; }

    // Setters
    public void setStatus(String status) { this.status = status; }
}
