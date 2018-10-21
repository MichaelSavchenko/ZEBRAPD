package com.zebrapd.usermanagement.converter;

import com.zebrapd.usermanagement.dto.TrainingDto;
import com.zebrapd.usermanagement.entity.Training;
import com.zebrapd.usermanagement.entity.TrainingPriceType;
import com.zebrapd.usermanagement.entity.TrainingType;
import com.zebrapd.usermanagement.error.exception.TrainingTypeNotFoundException;
import com.zebrapd.usermanagement.service.PriceService;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.zebrapd.usermanagement.entity.TrainingPriceType.*;
import static com.zebrapd.usermanagement.service.PriceService.*;
import static com.zebrapd.usermanagement.service.PriceService.FULL_TRAINING_LIMIT;

@Component
public class TrainingConverter {

    private PriceService priceService;

    public TrainingConverter(PriceService priceService) {
        this.priceService = priceService;
    }

    public Training convertToEntity(TrainingDto dto) {
        Training training = new Training();
        List<Integer> clientIds = dto.getClientIds();
        int numberOfClients = clientIds.size();
        TrainingType type = dto.getType();

        training.setClientIds(clientIds);
        training.setLocalDateTime(dto.getLocalDateTime());
        training.setTrainerId(dto.getTrainerId());
        training.setTrainingType(type);

        if (type.equals(TrainingType.PD)) {
            setPdReceipts(training, numberOfClients);
        } else if (type.equals(TrainingType.ST)) {
            setStRecipts(training, numberOfClients);
        } else if (type.equals(TrainingType.IN)) {
            setInRecipts(training, numberOfClients);
        } else {
            throw new TrainingTypeNotFoundException(String.format("Training type %s is unknown", type));
        }
        return training;
    }

    private void setInRecipts(Training training, int numberOfClients) {
        if (numberOfClients == 1) {
            int trainingPrice = priceService.getTrainingPrice(INDIVIDUAL);
            training.setReceipts(trainingPrice);
        } else if (numberOfClients == 2) {
            int trainingPrice = priceService.getTrainingPrice(INDIVIDUAL_2);
            training.setReceipts(trainingPrice);
        } else if (numberOfClients == 3) {
            int trainingPrice = priceService.getTrainingPrice(INDIVIDUAL_3);
            training.setReceipts(trainingPrice);
        } else {
            throw new TrainingTypeNotFoundException(String.format("Can't save %s clients for INDIVIDUAL training", numberOfClients));
        }
    }

    private void setStRecipts(Training training, int numberOfClients) {
        if (numberOfClients < FULL_TRAINING_LIMIT){
            training.setReceipts(getReceipt(STRETCHING_NOT_FULL));
        } else {
            training.setReceipts(getReceipt(STRETCHING));
        }
    }

    private void setPdReceipts(Training training, int numberOfClients) {
        if (numberOfClients < FULL_TRAINING_LIMIT){
            training.setReceipts(getReceipt(POLE_DANCE_NOT_FULL));
        } else {
            training.setReceipts(getReceipt(POLE_DANCE));
        }
    }

    private int getReceipt(TrainingPriceType type) {
        return priceService.getTrainingPrice(type);
    }
}
