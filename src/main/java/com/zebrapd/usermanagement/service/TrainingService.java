package com.zebrapd.usermanagement.service;

import com.zebrapd.usermanagement.dto.SaveTrainingResponseDto;
import com.zebrapd.usermanagement.entity.*;
import com.zebrapd.usermanagement.repositoty.TrainingRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
            int trainingsLeft = subtractOneTraining(trainingType, clientId);
            if (trainingsLeft <= 0) {
                Client clientById = clientService.getClientById(clientId);
                if (trainingsLeft == 0) {
                    result.addToLastTrainingLeft(clientById.getLastName());
                } else {
                    result.addNomoreTrainings(clientById.getLastName() + " " + trainingsLeft);
                }
            }
        }

        trainingRepository.createTraining(training);
        return result;
    }

    //todo change logic with training types
    private int subtractOneTraining(TrainingType trainingType, int clientId) {
        Subscription subscription = subscriptionService.getLastSubscriptionByType(clientId, trainingType);
        int trainingsLeft = (subscription.getNumberOfTrainings() - 1);
        subscriptionService.setNumberOfTrainings(trainingsLeft, subscription.getEntityId());
        return trainingsLeft;
    }

    //todo add logic of changing receipts
    public void addClient(int trainingId, int clientId, TrainingType trainingType) {
        trainingRepository.addClientsToTraining(trainingId, Collections.singletonList(clientId));
        subtractOneTraining(trainingType, clientId);
    }

    //todo add logic of changing receipts
    public void removeClient(int trainingId, int clientId, TrainingType trainingType) {
        trainingRepository.removeClientFromTraining(trainingId,clientId);
        addOneTraining(trainingType, clientId);
    }

    private void addOneTraining(TrainingType trainingType, int clientId) {
        Subscription subscription = subscriptionService.getLastSubscriptionByType(clientId, trainingType);
        int trainingsLeft = (subscription.getNumberOfTrainings() + 1);
        subscriptionService.setNumberOfTrainings(trainingsLeft, subscription.getEntityId());
    }
}
