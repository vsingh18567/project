package edu.upenn.cis573.project;

import java.io.Serializable;

public class Donation implements Serializable {

    private String fundName;
    private String contributorName;
    private double amount;
    private String date;

    public Donation(String fundName, String contributorName, double amount, String date) {
        this.fundName = fundName;
        this.contributorName = contributorName;
        this.amount = amount;
        this.date = date;
    }

    public String getFundName() {
        return fundName;
    }

    public String getContributorName() {
        return contributorName;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String toString() {
        return fundName + ": $" + amount + " on " + date;
    }


}