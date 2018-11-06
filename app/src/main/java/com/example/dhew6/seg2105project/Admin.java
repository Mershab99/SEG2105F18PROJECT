package com.example.dhew6.seg2105project;

import java.util.ArrayList;
import java.util.HashMap;

public class Admin extends User {

    private HashMap<String, Service> services;
    private static Admin instance = null;
    private ArrayList<Service> serviceList;

    private Admin(String name, String username, String password, String email) {
        super(name, username, password, email);
        this.services = new HashMap<>();
        this.serviceList = new ArrayList<>();

    }

    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin("admin", "admin", "admin", "admin");
        }
        return instance;
    }

    public HashMap<String, Service> getServices() {
        return services;
    }

    public void setServices(HashMap<String, Service> services) {
        this.services = services;
    }

    public void updateService(String key, Service service){
        services.put(key,service);
    }

    public ArrayList<Service> getServiceNames() {
        return serviceList;
    }

    public void setServiceNames(ArrayList<Service> serviceList) {
        this.serviceList = serviceList;
    }

    public void addServiceNames(Service serviceName){
        serviceList.add(serviceName);
    }

    @Override
    public String getType() {
        return "Admin";
    }
}
