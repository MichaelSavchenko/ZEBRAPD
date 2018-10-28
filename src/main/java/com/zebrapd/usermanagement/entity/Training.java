package com.zebrapd.usermanagement.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Training {
    private Integer entityId;
    private TrainingReceiptType trainingReceiptType;
    private int receipts;
    private LocalDateTime localDateTime;
    private int trainerId;
    private List<Integer> clientIds;

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public TrainingReceiptType getTrainingReceiptType() {
        return trainingReceiptType;
    }

    public void setTrainingReceiptType(TrainingReceiptType trainingReceiptType) {
        this.trainingReceiptType = trainingReceiptType;
    }

    public int getReceipts() {
        return receipts;
    }

    public void setReceipts(int receipts) {
        this.receipts = receipts;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public int getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(int trainerId) {
        this.trainerId = trainerId;
    }

    public List<Integer> getClientIds() {
        return clientIds;
    }

    public void setClientIds(List<Integer> clientIds) {
        this.clientIds = clientIds;
    }
}
