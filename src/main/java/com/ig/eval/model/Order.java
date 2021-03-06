package com.ig.eval.model;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;

import java.sql.Timestamp;
import java.util.List;

public class Order {
    private int orderId;
    private String customerPhoneNumber;
    private String customerName;
    private List<OrderItem> itemList;
    private int customerId;
    private Timestamp orderTimeStamp;

    public List<OrderItem> getItemList() {
        return ListUtils.emptyIfNull(itemList);
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
