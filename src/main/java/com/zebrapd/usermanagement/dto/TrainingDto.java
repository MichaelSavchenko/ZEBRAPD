package com.zebrapd.usermanagement.dto;

import com.zebrapd.usermanagement.entity.TrainingType;

import java.time.LocalDateTime;
import java.util.List;

public class TrainingDto {
    private TrainingType type;
    private LocalDateTime localDateTime;
    private int trainerId;
    private List<Integer> clientIds;

    public TrainingType getType() {
        return type;
    }

    public void setType(TrainingType type) {
        this.type = type;
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
