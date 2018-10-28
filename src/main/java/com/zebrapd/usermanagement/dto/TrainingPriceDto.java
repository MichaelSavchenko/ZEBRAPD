package com.zebrapd.usermanagement.dto;

import com.zebrapd.usermanagement.entity.TrainingReceiptType;

public class TrainingPriceDto {
    private TrainingReceiptType trainingReceiptType;
    private int price;

    public TrainingReceiptType getTrainingReceiptType() {
        return trainingReceiptType;
    }

    public void setTrainingReceiptType(TrainingReceiptType trainingReceiptType) {
        this.trainingReceiptType = trainingReceiptType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
