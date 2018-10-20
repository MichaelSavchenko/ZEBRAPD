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
    private ClientService clientService;

    public TrainingService(
            TrainingRepository trainingRepository,
            SubscriptionService subscriptionService,
            ClientService clientService
    ) {
        this.trainingRepository = trainingRepository;
        this.subscriptionService = subscriptionService;
        this.clientService = clientService;
    }

    public SaveTrainingResponseDto saveTraining(Training training) {
        SaveTrainingResponseDto result = new SaveTrainingResponseDto();
        List<Integer> clientIds = training.getClientIds();
        TrainingType trainingType = training.getTrainingType();

        for (int clientId : clientIds) {
            Subscription subscription = subscriptionService.getLastSubscriptionByType(clientId, trainingType);
            int trainingsLeft = (subscription.getNumberOfTrainings() - 1);
            if (trainingsLeft <= 0) {
                Client clientById = clientService.getClientById(clientId);
                if (trainingsLeft == 0) {
                    result.addToLastTrainingLeft(clientById.getLastName());
                } else {
                    result.addNomoreTrainings(clientById.getLastName() + " " + trainingsLeft);
                }
            }
            subscriptionService.setNumberOfTrainings(trainingsLeft, subscription.getEntityId());
        }

        trainingRepository.createTraining(training);
        return result;
    }
}
