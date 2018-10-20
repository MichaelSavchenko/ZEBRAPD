package com.zebrapd.usermanagement.dto;

import com.zebrapd.usermanagement.entity.TrainingType;

public class TrainingPriceDto {
    private TrainingType trainingType;
    private int price;

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
