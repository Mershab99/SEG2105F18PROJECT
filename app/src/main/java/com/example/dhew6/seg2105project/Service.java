package com.example.dhew6.seg2105project;

public class Service {

    private String name;
    private double rate;

    /**
     * inits the Service object and sets values
     *
     * @param name
     * @param rate
     */
    public Service(String name, double rate) {
        this.name = name;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

}
