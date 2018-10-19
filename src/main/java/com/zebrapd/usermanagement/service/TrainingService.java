package com.zebrapd.usermanagement.service;

import com.zebrapd.usermanagement.dto.SaveTrainingResponseDto;
import com.zebrapd.usermanagement.entity.Client;
import com.zebrapd.usermanagement.entity.Subscription;
import com.zebrapd.usermanagement.entity.Training;
import com.zebrapd.usermanagement.entity.TrainingType;
import com.zebrapd.usermanagement.repositoty.TrainingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingService {
    
    private TrainingRepository trainingRepository;
    private SubscriptionService subscriptionService;

    public TrainingService(TrainingRepository trainingRepository, SubscriptionService subscriptionService) {
        this.trainingRepository = trainingRepository;
        this.subscriptionService = subscriptionService;
    }
    
    public SaveTrainingResponseDto saveTraining(Training training){
        SaveTrainingResponseDto result = new SaveTrainingResponseDto();
        List<Client> clients = training.getClients();
        TrainingType trainingType = training.getTrainingType();

        for (Client client: clients) {
            Subscription subscription = subscriptionService.getCurrentSubscriptionByType(client.getEntityId(), trainingType);
            int trainingsLeft = subscription.getNumberOfTrainings() - 1;
            if (trainingsLeft == 0){
                result.addToLastTrainingLeft(client.getLastName());
            } else if (trainingsLeft < 0) {
                result.addNomoreTrainings(client.getLastName() + " " + trainingsLeft);
            }
            subscriptionService.setNumberOftrainings(trainingsLeft, subscription.getEntityId());
        }



        return result;
    }
}
