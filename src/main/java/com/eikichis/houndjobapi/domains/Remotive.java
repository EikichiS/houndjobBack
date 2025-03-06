package com.eikichis.houndjobapi.domains;

public class Remotive {
    private int id;
    private String url;
    private String title;
    private String company_name;
    private String candidate_required_location;
    private String publication_date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCandidate_required_location() {
        return candidate_required_location;
    }

    public void setCandidate_required_location(String candidate_required_location) {
        this.candidate_required_location = candidate_required_location;
    }

    public String getPublication_date() {
        return publication_date;
    }

    public void setPublication_date(String publication_date) {
        this.publication_date = publication_date;
    }
}
