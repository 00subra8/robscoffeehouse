package com.ig.eval.model;

public class Report {

    private String date;
    private String variety;
    private String initialAvailability;
    private String quantitySold;

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getInitialAvailability() {
        return initialAvailability;
    }

    public void setInitialAvailability(String initialAvailability) {
        this.initialAvailability = initialAvailability;
    }

    public String getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(String quantitySold) {
        this.quantitySold = quantitySold;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
