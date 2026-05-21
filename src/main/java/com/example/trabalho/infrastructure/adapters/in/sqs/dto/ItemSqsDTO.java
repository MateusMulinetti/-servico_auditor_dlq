package com.example.trabalho.infrastructure.adapters.in.sqs.dto;

import java.util.List;

public class ItemSqsDTO {
    private String zipCode;
    private Long customerId;
    private List<MensagemSqsDTO> orderItems;
    private String origin;
    private String occurredAt;

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public List<MensagemSqsDTO> getOrderItems() { return orderItems; }
    public void setOrderItems(List<MensagemSqsDTO> orderItems) { this.orderItems = orderItems; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getOccurredAt() { return occurredAt; }
    public void setOccurredAt(String occurredAt) { this.occurredAt = occurredAt; }
}