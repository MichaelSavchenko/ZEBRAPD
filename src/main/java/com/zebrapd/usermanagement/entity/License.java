package com.zebrapd.usermanagement.entity;

import java.time.LocalDate;

public class License {
    private Integer entityId;
    private int clientId;
    private TrainingType type;
    private int numberOfTrainings;
    private int price;
    private LocalDate startDate;
    private LocalDate expiratioDate;

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public TrainingType getType() {
        return type;
    }

    public void setType(TrainingType type) {
        this.type = type;
    }

    public int getNumberOfTrainings() {
        return numberOfTrainings;
    }

    public void setNumberOfTrainings(int numberOfTrainings) {
        this.numberOfTrainings = numberOfTrainings;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getExpiratioDate() {
        return expiratioDate;
    }

    public void setExpiratioDate(LocalDate expiratioDate) {
        this.expiratioDate = expiratioDate;
    }
}
