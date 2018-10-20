package com.zebrapd.usermanagement.service;

import com.zebrapd.usermanagement.entity.TrainingType;
import com.zebrapd.usermanagement.error.exception.TrainingTypeNotFoundException;
import com.zebrapd.usermanagement.repositoty.TrainingRepository;
import org.springframework.stereotype.Service;


@Service
public class PriceService {

    private final static int FULL_TRAINING_LIMIT = 6;

    private TrainingRepository trainingRepository;

    public PriceService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    public void setTrainingPrice(TrainingType trainingType, int price) {
        trainingRepository.setTrainingPrice(trainingType, price);
    }

    public int getTrainingPrice(TrainingType trainingType, int numberOfClients) {

        if (trainingType.equals(TrainingType.PD) && numberOfClients < FULL_TRAINING_LIMIT){
          trainingType = TrainingType.PD_NOT_FULL;
        }

        if (trainingType.equals(TrainingType.STRECHING) && numberOfClients < FULL_TRAINING_LIMIT){
            trainingType = TrainingType.STRECHING_NOT_FULL;
        }

        Integer trainingPrice = trainingRepository.getTrainingPrice(trainingType);
        if (trainingPrice == null) {
            throw new TrainingTypeNotFoundException(String.format("Training type %s not founded", trainingType));
        }
        return trainingPrice;
    }

    public int getTrainingPriceForAdmin(TrainingType type) {
        Integer trainingPrice = trainingRepository.getTrainingPrice(type);
        if (trainingPrice == null) {
            throw new TrainingTypeNotFoundException(String.format("Training type %s not founded", type));
        }
        return trainingPrice;
    }
}
