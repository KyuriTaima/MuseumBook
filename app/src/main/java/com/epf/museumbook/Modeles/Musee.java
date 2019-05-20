package com.epf.museumbook.Modeles;

public class Musee {
    private String address;
    private String pc;
    private String department;
    private Boolean closed;
    private String annual_closing;
    private String region;
    private String website;
    private String city;

    public Musee(String address, String pc, String department, Boolean closed, String annual_closing, String region, String website, String city) {
        this.address = address;
        this.pc = pc;
        this.department = department;
        this.closed = closed;
        this.annual_closing = annual_closing;
        this.region = region;
        this.website = website;
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPc(String pc) {
        this.pc = pc;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public void setAnnual_closing(String annual_closing) {
        this.annual_closing = annual_closing;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public String getPc() {
        return pc;
    }

    public String getDepartment() {
        return department;
    }

    public Boolean getClosed() {
        return closed;
    }

    public String getAnnual_closing() {
        return annual_closing;
    }

    public String getRegion() {
        return region;
    }

    public String getWebsite() {
        return website;
    }

    public String getCity() {
        return city;
    }
}
