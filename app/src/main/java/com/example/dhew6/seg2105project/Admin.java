package com.example.dhew6.seg2105project;

public class Admin extends User {

    public Admin(String name, String username, String password, String email) {
        super(name, username, password, email);
    }

    /**
     * overrides the User java
     * returns a string representation of the type of this class
     *
     * @return {String}
     */
    @Override
    public String getType() {
        return "Admin";
    }
}
