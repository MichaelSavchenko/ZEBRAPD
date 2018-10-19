package com.zebrapd.usermanagement.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class Training{
    private Integer entityId;
    private TrainingType trainingType;
    private int receipts;
    private LocalDateTime localDateTime;
    private Trainer trainer;
    private List<Client> clients;

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

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
}
