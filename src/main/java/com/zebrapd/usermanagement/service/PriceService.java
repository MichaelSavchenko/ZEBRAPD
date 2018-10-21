package com.zebrapd.usermanagement.service;

import com.zebrapd.usermanagement.entity.TrainingPriceType;
import com.zebrapd.usermanagement.error.exception.TrainingTypeNotFoundException;
import com.zebrapd.usermanagement.repositoty.TrainingRepository;
import org.springframework.stereotype.Service;


@Service
public class PriceService {

    public final static int FULL_TRAINING_LIMIT = 6;

    private TrainingRepository trainingRepository;

    public PriceService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    public void setTrainingPrice(TrainingPriceType trainingPriceType, int price) {
        trainingRepository.setTrainingPrice(trainingPriceType, price);
    }


    public int getTrainingPrice(TrainingPriceType type) {
        Integer trainingPrice = trainingRepository.getTrainingPrice(type);
        if (trainingPrice == null) {
            throw new TrainingTypeNotFoundException(String.format("Training type %s not founded", type));
        }
        return trainingPrice;
    }
}
