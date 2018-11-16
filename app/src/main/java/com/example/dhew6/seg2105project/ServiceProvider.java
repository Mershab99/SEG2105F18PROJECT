package com.example.dhew6.seg2105project;

import java.util.ArrayList;
import java.util.HashMap;

public class ServiceProvider extends User{

    private HashMap<String, ArrayList<String>> timeMap; //available time slots
    private ArrayList<Service> services;
    private String address;
    private String phone;
    private String desc;
    private String company;
    private boolean licensed;

    public ServiceProvider(String name, String username, String password, String email) {
        super(name, username, password, email);
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isLicensed() {
        return licensed;
    }

    public void setLicensed(boolean licensed) {
        this.licensed = licensed;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * @return String representation of userType
     */
    @Override
    public String getType() {
        return "Service Provider";
    }

    public HashMap<String, ArrayList<String>> getTimeMap() {
        return timeMap;
    }

    public void setTimeMap(HashMap<String, ArrayList<String>> timeMap) {
        this.timeMap = timeMap;
    }


}
