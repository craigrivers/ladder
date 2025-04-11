package com.example.demo.domain;

public class Court {
    private int courtId;
    private String name;
    private String address;
    private String link;
    private String phone;

    public Court() {}

    public Court(int courtId, String name, String address, String link, String phone) {
        this.courtId = courtId;
        this.name = name;
        this.address = address;
        this.link = link;
        this.phone = phone;
    }

    // Getters and Setters
    public int getCourtId() { return courtId; }
    public void setCourtId(int courtId) { this.courtId = courtId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
} 