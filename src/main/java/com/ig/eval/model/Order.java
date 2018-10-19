package com.ig.eval.model;

import java.sql.Timestamp;
import java.util.List;

public class Order {
    private String customerPhoneNumber;
    private String customerName;
    private List<OrderItem> itemList;
    private int customerId;
    private int customerHistoricalOrderNumber;
    private int orderId;
    private Timestamp orderTimeStamp;

    public List<OrderItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<OrderItem> itemList) {
        this.itemList = itemList;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerHistoricalOrderNumber() {
        return customerHistoricalOrderNumber;
    }

    public void setCustomerHistoricalOrderNumber(int customerHistoricalOrderNumber) {
        this.customerHistoricalOrderNumber = customerHistoricalOrderNumber;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Timestamp getOrderTimeStamp() {
        return orderTimeStamp;
    }

    public void setOrderTimeStamp(Timestamp orderTimeStamp) {
        this.orderTimeStamp = orderTimeStamp;
    }
}
