package com.zebrapd.usermanagement.service;

import com.zebrapd.usermanagement.entity.TrainingReceiptType;
import com.zebrapd.usermanagement.error.exception.TrainingTypeNotFoundException;
import com.zebrapd.usermanagement.repositoty.TrainingRepository;
import org.springframework.stereotype.Service;


@Service
public class PriceService {

    public final static int FULL_TRAINING_LIMIT = 5;
    final static int INDIVIDUAL_TRAINING_LIMIT = 3;

    private TrainingRepository trainingRepository;

    public PriceService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    public void setTrainingPrice(TrainingReceiptType trainingReceiptType, int price) {
        trainingRepository.setTrainingPrice(trainingReceiptType, price);
    }


    public int getTrainingReciept(TrainingReceiptType type) {
        Integer trainingPrice = trainingRepository.getTrainingReceipt(type);
        if (trainingPrice == null) {
            throw new TrainingTypeNotFoundException(String.format("Training type %s not founded", type));
        }
        return trainingPrice;
    }
}
