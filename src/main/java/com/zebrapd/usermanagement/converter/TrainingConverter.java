package com.zebrapd.usermanagement.converter;

import com.zebrapd.usermanagement.dto.TrainingDto;
import com.zebrapd.usermanagement.entity.Training;
import com.zebrapd.usermanagement.entity.TrainingReceiptType;
import com.zebrapd.usermanagement.entity.TrainingType;
import com.zebrapd.usermanagement.error.exception.TrainingTypeNotFoundException;
import com.zebrapd.usermanagement.service.PriceService;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.zebrapd.usermanagement.entity.TrainingReceiptType.*;
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
        setTrainingType(training, numberOfClients, type);
        training.setReceipts(getReceipt(training.getTrainingReceiptType()));

        return training;
    }



    private void setTrainingType(Training training, int numberOfClients, TrainingType type) {
        if (type.equals(TrainingType.PD)) {
            setPdType(training, numberOfClients);
        } else if (type.equals(TrainingType.ST)) {
            setStType(training, numberOfClients);
        } else if (type.equals(TrainingType.IN)) {
            setInType(training, type);
        } else {
            throw new TrainingTypeNotFoundException(String.format("Training type %s is unknown", type));
        }
    }

    private void setInType(Training training, TrainingType type) {
        if (type.equals(TrainingType.IN)) {
            training.setTrainingReceiptType(INDIVIDUAL);
        }
        if (type.equals(TrainingType.IN_2)) {
            training.setTrainingReceiptType(INDIVIDUAL_2);
        }
        if (type.equals(TrainingType.IN_3)) {
            training.setTrainingReceiptType(INDIVIDUAL_3);
        }
    }

    private void setStType(Training training, int numberOfClients) {
        if (numberOfClients > FULL_TRAINING_LIMIT) {
            training.setTrainingReceiptType(STRETCHING);
        } else training.setTrainingReceiptType(STRETCHING_NOT_FULL);
    }

    private void setPdType(Training training, int numberOfClients) {
        if (numberOfClients > FULL_TRAINING_LIMIT) {
            training.setTrainingReceiptType(POLE_DANCE);
        } else training.setTrainingReceiptType(POLE_DANCE_NOT_FULL);
    }

    private int getReceipt(TrainingReceiptType type) {
        return priceService.getTrainingReciept(type);
    }
}
