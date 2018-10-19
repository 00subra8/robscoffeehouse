package com.ig.eval.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rch")
public class ApplicationProperties {

    private String welcome;
    private String coffeeHouseName;
    private String tagLine;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String bottomLine;
    private String vatPercentage;

    public String getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }


    public String getCoffeeHouseName() {
        return coffeeHouseName;
    }

    public void setCoffeeHouseName(String coffeeHouseName) {
        this.coffeeHouseName = coffeeHouseName;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getBottomLine() {
        return bottomLine;
    }

    public void setBottomLine(String bottomLine) {
        this.bottomLine = bottomLine;
    }

    public String getVatPercentage() {
        return vatPercentage;
    }

    public void setVatPercentage(String vatPercentage) {
        this.vatPercentage = vatPercentage;
    }
}
