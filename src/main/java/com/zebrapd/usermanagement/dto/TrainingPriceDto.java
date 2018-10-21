package com.zebrapd.usermanagement.dto;

import com.zebrapd.usermanagement.entity.TrainingPriceType;

public class TrainingPriceDto {
    private TrainingPriceType trainingPriceType;
    private int price;

    public TrainingPriceType getTrainingPriceType() {
        return trainingPriceType;
    }

    public void setTrainingPriceType(TrainingPriceType trainingPriceType) {
        this.trainingPriceType = trainingPriceType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
