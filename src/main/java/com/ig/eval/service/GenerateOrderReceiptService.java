package com.ig.eval.service;

import com.ig.eval.configuration.ApplicationProperties;
import com.ig.eval.dao.CoffeeHouseDAO;
import com.ig.eval.exception.CoffeeHouseInputException;
import com.ig.eval.exception.CoffeeHouseInternalException;
import com.ig.eval.model.Order;
import com.ig.eval.model.OrderItem;
import com.ig.eval.model.ReceiptBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class GenerateOrderReceiptService {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ReceiptBuilder receiptBuilder;

    @Autowired
    private CoffeeHouseDAO coffeeHouseDAO;

    public String getReceipt(Order order) {
        if (order == null
                || order.getOrderId() == 0) {
            throw new CoffeeHouseInputException("Error while trying to generate receipt. No Order details found");
        }

        receiptBuilder.addEntry(String.format("\t\t\t\t\t\t%s", applicationProperties.getCoffeeHouseName()))
                .addEntry(getNewLine())
                .addEntry(String.format("\t\t %s", applicationProperties.getTagLine()))
                .addEntry(getNewLine())
                .addEntry(String.format("\t\t\t\t\t\t%s", applicationProperties.getAddressLine1()))
                .addEntry(getNewLine())
                .addEntry(String.format("\t\t\t\t\t\t%s", applicationProperties.getAddressLine2()))
                .addEntry(getNewLine())
                .addEntry(String.format("\t\t\t\t\t\t%s", applicationProperties.getAddressLine3()))
                .addEntry(getNewLine())
                .addEntry(getNewLine())
                .addEntry(String.format("OrderId: %s", order.getOrderId()))
                .addEntry(getNewLine())
                .addEntry(String.format("Time Of Order: %s", order.getOrderTimeStamp()))
                .addEntry(getNewLine())
                .addEntry(String.format("Customer Details: %s %s", order.getCustomerName(), order.getCustomerPhoneNumber()))
                .addEntry(getNewLine())
                .addEntry(getNewLine())
                .addEntry(getNewLine())
                .addEntry(String.format("Item%-25sQuantity%-18sPrice", " ", " "))
                .addEntry(getNewLine());

        populateItemList(order.getItemList());


        return receiptBuilder
                .addEntry(String.format("\t\t\t %s", applicationProperties.getBottomLine()))
                .addEntry(getNewLine())
                .build();
    }

    private void populateItemList(List<OrderItem> itemList) {
        if (CollectionUtils.isEmpty(itemList)) {
            throw new CoffeeHouseInternalException("Error while generating receipt. It's on the house");
        }
        AtomicReference<Double> totalPrice = new AtomicReference<>(0.0);

        itemList.stream()
                .filter(Objects::nonNull)
                .forEach(orderItem -> addLineItem(totalPrice, orderItem));

        addBillAmount(totalPrice);
    }

    private void addBillAmount(AtomicReference<Double> totalPrice) {
        double vatAmount = 0.0;
        if (totalPrice.get() > 0) {
            vatAmount = (totalPrice.get() * Double.valueOf(applicationProperties.getVatPercentage())) / 100;
        }

        double billAmount = totalPrice.get() + vatAmount;

        receiptBuilder.addEntry(getNewLine())
                .addEntry(String.format("Total Price: %s", totalPrice))
                .addEntry(getNewLine())
                .addEntry(String.format("VAT (%s%%): %s", applicationProperties.getVatPercentage(), vatAmount))
                .addEntry(getNewLine())
                .addEntry(getNewLine())
                .addEntry(String.format("BILL AMOUNT: %s", billAmount))
                .addEntry(getNewLine())
                .addEntry(getNewLine());
    }

    private void addLineItem(AtomicReference<Double> totalPrice, OrderItem orderItem) {
        double price = coffeeHouseDAO.getPrice(orderItem.getCoffeeVarietyName());
        double itemPrice = price * Integer.valueOf(orderItem.getQuantity());
        totalPrice.updateAndGet(tp -> tp + itemPrice);

        receiptBuilder.addEntry(String.format("%s%-25s%s%-25s%s", orderItem.getCoffeeVarietyName(), " ", orderItem.getQuantity(),
                " ", itemPrice))
                .addEntry(getNewLine());
    }

    private String getNewLine() {
        return String.format("%n");
    }
}
