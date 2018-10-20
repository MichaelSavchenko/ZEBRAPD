package com.zebrapd.usermanagement.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class Training{
    private Integer entityId;
    private TrainingType trainingType;
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

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
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
