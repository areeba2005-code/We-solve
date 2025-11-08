// src/com/wesolve/model/Report.java
package com.wesolve.model;

import java.sql.Timestamp;

public class Report {
    private int id;
    private int userId;
    private String title;
    private String description;
    private String imagePath;
    private String location;
    private String status;
    private Timestamp createdAt;

    public Report() {}

    public Report(String title, String description, String location, String imagePath, String status) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.imagePath = imagePath;
        this.status = status;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return title + " - " + location;
    }
}