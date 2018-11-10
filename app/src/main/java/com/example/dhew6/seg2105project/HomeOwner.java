package com.example.dhew6.seg2105project;

public class HomeOwner extends User {

    public HomeOwner(String name, String username, String password, String email) {
        super(name, username, password, email);
    }

    /**
     * @return String representation of userType
     */
    @Override
    public String getType() {
        return "Home Owner";
    }
}
