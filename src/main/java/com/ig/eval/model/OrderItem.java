package com.ig.eval.model;

public class OrderItem {

    private String coffeeVarietyName;
    private String quantity;
    private int coffeeVarietyId;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCoffeeVarietyName() {
        return coffeeVarietyName;
    }

    public void setCoffeeVarietyName(String coffeeVarietyName) {
        this.coffeeVarietyName = coffeeVarietyName;
    }

    public int getCoffeeVarietyId() {
        return coffeeVarietyId;
    }

    public void setCoffeeVarietyId(int coffeeVarietyId) {
        this.coffeeVarietyId = coffeeVarietyId;
    }
}
