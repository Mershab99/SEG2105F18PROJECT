package com.example.dhew6.seg2105project;

import java.util.HashMap;

public class Admin extends User {

    private HashMap<String, Service> services;
    private static Admin instance = null;

    private Admin(String name, String username, String password, String email) {
        super(name, username, password, email);
        this.services = new HashMap<>();

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

    @Override
    public String getType() {
        return "Admin";
    }
}
