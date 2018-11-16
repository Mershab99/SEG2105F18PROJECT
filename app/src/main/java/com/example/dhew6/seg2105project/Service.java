package com.example.dhew6.seg2105project;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return Double.compare(service.rate, rate) == 0 &&
                Objects.equals(name, service.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, rate);
    }
}
