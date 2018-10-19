package com.ig.eval.model;

public class ReceiptBuilder {

    private StringBuilder contentBuilder = new StringBuilder();

    public ReceiptBuilder addEntry(String entry) {
        contentBuilder.append(entry);
        return this;
    }

    public String build() {
        return contentBuilder.toString();
    }
}
