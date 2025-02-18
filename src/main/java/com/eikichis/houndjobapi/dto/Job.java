package com.eikichis.houndjobapi.dto;

public class Job {
    private String title;
    private String company;
    private String location;
    private String detailUrl;

    public Job(String title, String company, String location, String detailUrl) {
        this.title = title;
        this.company = company;
        this.location = location;
        this.detailUrl = detailUrl;
    }

    // Getters y setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDetailUrl() { return detailUrl; }
    public void setDetailUrl(String detailUrl) { this.detailUrl = detailUrl; }
}
