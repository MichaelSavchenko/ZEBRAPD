package com.zebrapd.usermanagement.entity;

import java.time.LocalDate;
import java.util.Set;

public class Training{
    private Integer entityId;
    private int receipts;
    private LocalDate date;
    private Trainer trainer;
    private Set<Client> clients;

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public int getReceipts() {
        return receipts;
    }

    public void setReceipts(int receipts) {
        this.receipts = receipts;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }
}
