package com.example.recycleease_customerapp;

public class BuybackRequest {
    private String requestId;
    private String name;
    private String phone;
    private String deviceType;
    private String deviceCondition;
    private String pickupDate;
    private String address;
    private String status;

    public BuybackRequest() {
        // Default constructor required for Firebase
    }

    public BuybackRequest(String requestId, String name, String phone, String deviceType, String deviceCondition, String pickupDate, String address, String status) {
        this.requestId = requestId;
        this.name = name;
        this.phone = phone;
        this.deviceType = deviceType;
        this.deviceCondition = deviceCondition;
        this.pickupDate = pickupDate;
        this.address = address;
        this.status = status;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getDeviceCondition() {
        return deviceCondition;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public String getAddress() {
        return address;
    }

    public String getStatus() {
        return status;
    }
}
