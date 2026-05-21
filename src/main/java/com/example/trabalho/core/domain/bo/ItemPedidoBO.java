package com.example.trabalho.core.domain.bo;

import java.util.List;

public class ItemPedidoBO {
    private final String zipCode;
    private final Long customerId;
    private final List<RegistroFalhaBO> orderItems;
    private final String origin;
    private final String occurredAt;
    private final String rawJsonPayload;

    public ItemPedidoBO(String zipCode, Long customerId, List<RegistroFalhaBO> orderItems, String origin, String occurredAt, String rawJsonPayload) {
        this.zipCode = zipCode;
        this.customerId = customerId;
        this.orderItems = orderItems;
        this.origin = origin;
        this.occurredAt = occurredAt;
        this.rawJsonPayload = rawJsonPayload;
    }

    public String calculateSeverity() {
        if (orderItems == null || orderItems.isEmpty()) {
            return "LOW";
        }

        int totalAmount = 0;
        for (RegistroFalhaBO item : orderItems) {
            totalAmount += item.getAmount();
        }

        if (totalAmount > 100) {
            return "HIGH";
        } else if (totalAmount >= 50) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }

    public String getZipCode() { return zipCode; }
    public Long getCustomerId() { return customerId; }
    public List<RegistroFalhaBO> getOrderItems() { return orderItems; }
    public String getOrigin() { return origin; }
    public String getOccurredAt() { return occurredAt; }
    public String getRawJsonPayload() { return rawJsonPayload; }
}