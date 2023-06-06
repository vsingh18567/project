package edu.upenn.cis573.project;

public class Fund {

    private String id;
    private String name;
    private double target;
    private double totalDonations;

    public Fund(String id, String name, double target, double totalDonations) {
        this.id = id;
        this.name = name;
        this.target = target;
        this.totalDonations = totalDonations;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getTarget() {
        return target;
    }

    public double getTotalDonations() {
        return totalDonations;
    }
}

