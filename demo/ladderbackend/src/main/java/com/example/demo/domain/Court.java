package com.example.demo.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

/**
 * Represents a tennis court in the ladder system.
 */
public class Court {
    @Positive
    private int courtId;

    @NotBlank(message = "Court name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @Pattern(regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?$", 
             message = "Invalid URL format")
    private String link;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", 
             message = "Invalid phone number format")
    private String phone;

    /**
     * Default constructor required for JPA and JSON serialization
     */
    public Court() {
    }

    /**
     * Creates a new Court instance with the specified details.
     *
     * @param courtId Unique identifier for the court
     * @param name Name of the court
     * @param address Physical address of the court
     * @param link URL to the court's website
     * @param phone Contact phone number
     */
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

    @Override
    public String toString() {
        return "Court{" +
                "courtId=" + courtId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", link='" + link + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
} 