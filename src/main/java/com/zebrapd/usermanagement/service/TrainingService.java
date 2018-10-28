package com.zebrapd.usermanagement.service;

import com.zebrapd.usermanagement.dto.SaveTrainingResponseDto;
import com.zebrapd.usermanagement.entity.*;
import com.zebrapd.usermanagement.error.exception.CanNotChangeTrainingTypeException;
import com.zebrapd.usermanagement.error.exception.CanNotRemoveClientException;
import com.zebrapd.usermanagement.error.exception.TrainingTypeNotFoundException;
import com.zebrapd.usermanagement.repositoty.TrainingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.zebrapd.usermanagement.entity.TrainingReceiptType.*;
import static com.zebrapd.usermanagement.entity.TrainingType.*;
import static com.zebrapd.usermanagement.service.PriceService.FULL_TRAINING_LIMIT;
import static com.zebrapd.usermanagement.service.PriceService.INDIVIDUAL_TRAINING_LIMIT;
import static com.zebrapd.usermanagement.utils.TrainingTypeUtils.of;
import static java.lang.String.format;
import static java.util.Collections.singletonList;

@Service
public class TrainingService {

    private TrainingRepository trainingRepository;
    private SubscriptionService subscriptionService;
    private ClientService clientService;
    private PriceService priceService;

    public TrainingService(
            TrainingRepository trainingRepository,
            SubscriptionService subscriptionService,
            ClientService clientService,
            PriceService priceService
    ) {
        this.trainingRepository = trainingRepository;
        this.subscriptionService = subscriptionService;
        this.clientService = clientService;
        this.priceService = priceService;
    }

    public SaveTrainingResponseDto saveTraining(Training training) {
        SaveTrainingResponseDto result = new SaveTrainingResponseDto();
        List<Integer> clientIds = training.getClientIds();
        TrainingReceiptType trainingReceiptType = training.getTrainingReceiptType();

        for (int clientId : clientIds) {
            int trainingsLeft = subtractOneTrainingFromSubscription(trainingReceiptType, clientId);
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

    public Training getTraining(int id) {
        return trainingRepository.getTraining(id);
    }

    private int subtractOneTrainingFromSubscription(TrainingReceiptType trainingReceiptType, int clientId) {
        Optional<Subscription> lastSubscriptionByType = subscriptionService.getActiveSubscription(clientId, of(trainingReceiptType));
        if (lastSubscriptionByType.isPresent()) {
            Subscription subscription = lastSubscriptionByType.get();
            int trainingsLeft = (subscription.getNumberOfTrainings() - 1);
            subscriptionService.setNumberOfTrainings(trainingsLeft, subscription.getEntityId());
            return trainingsLeft;
        } else
            throw new TrainingTypeNotFoundException(format("Client %s has no subscription of type %s", clientId, trainingReceiptType));
    }

    @Transactional
    public void addClient(int trainingId, int clientId) {
        Training training = trainingRepository.getTraining(trainingId);
        TrainingReceiptType trainingReceiptType = training.getTrainingReceiptType();

        int clientsCount = training.getClientIds().size();
        if (trainingReceiptType.equals(POLE_DANCE_NOT_FULL) && clientsCount == (FULL_TRAINING_LIMIT)) {
            setReciptAndType(training, POLE_DANCE);
            trainingRepository.updateTraining(training);
        } else if (trainingReceiptType.equals(STRETCHING_NOT_FULL) && clientsCount == (FULL_TRAINING_LIMIT)) {
            setReciptAndType(training, STRETCHING);
            trainingRepository.updateTraining(training);
        } else if (trainingReceiptType.equals(INDIVIDUAL)) {
            setReciptAndType(training, INDIVIDUAL_2);
            trainingRepository.updateTraining(training);
        } else if (trainingReceiptType.equals(INDIVIDUAL_2)) {
            setReciptAndType(training, INDIVIDUAL_3);
            trainingRepository.updateTraining(training);
        }

        trainingRepository.addClientsToTraining(trainingId, singletonList(clientId));
        subtractOneTrainingFromSubscription(training.getTrainingReceiptType(), clientId);
    }

    private void setReciptAndType(Training training, TrainingReceiptType individual3) {
        int trainingPrice = priceService.getTrainingReciept(individual3);
        training.setReceipts(trainingPrice);
        training.setTrainingReceiptType(individual3);
    }

    @Transactional
    public void removeClient(int trainingId, int clientId) {
        Training training = trainingRepository.getTraining(trainingId);

        training.getClientIds().stream()
                .filter(id -> id.equals(trainingId))
                .findAny()
                .orElseThrow(() -> new CanNotRemoveClientException(format(
                        "Client %s can't be removed from training %s, there was no such client on this training",
                        clientId, trainingId)));
        TrainingReceiptType trainingReceiptType = training.getTrainingReceiptType();
        int clientsCount = training.getClientIds().size();
        if (clientsCount == 1) {
            throw new CanNotRemoveClientException(format("Last client %s can't be removed from training %s", clientId, trainingId));
        }

        if (trainingReceiptType.equals(POLE_DANCE) && clientsCount == (FULL_TRAINING_LIMIT + 1)) {
            setReciptAndType(training, POLE_DANCE_NOT_FULL);
            trainingRepository.updateTraining(training);
        } else if (trainingReceiptType.equals(STRETCHING) && clientsCount == (FULL_TRAINING_LIMIT + 1)) {
            setReciptAndType(training, POLE_DANCE);
            trainingRepository.updateTraining(training);
        } else if (trainingReceiptType.equals(INDIVIDUAL_3)) {
            setReciptAndType(training, INDIVIDUAL_2);
            trainingRepository.updateTraining(training);
        } else if (trainingReceiptType.equals(INDIVIDUAL_2)) {
            setReciptAndType(training, INDIVIDUAL);
            trainingRepository.updateTraining(training);
        }

        trainingRepository.removeClientFromTraining(trainingId, clientId);
        addOneTrainingToSubscription(training.getTrainingReceiptType(), clientId);
    }

    private void addOneTrainingToSubscription(TrainingReceiptType trainingReceiptType, int clientId) {
        Optional<Subscription> lastSubscriptionByType = subscriptionService.getActiveSubscription(clientId, of(trainingReceiptType));
        Subscription subscription = lastSubscriptionByType
                .orElseThrow(
                        () -> new TrainingTypeNotFoundException(format("Training type not found %s", trainingReceiptType)));
        int trainingsLeft = (subscription.getNumberOfTrainings() + 1);
        subscriptionService.setNumberOfTrainings(trainingsLeft, subscription.getEntityId());
    }

    @Transactional
    public void changeTrainingType(int trainingId, TrainingType newUiType) {
        Training training = trainingRepository.getTraining(trainingId);
        TrainingReceiptType oldType = training.getTrainingReceiptType();
        int clientsCount = training.getClientIds().size();

        validateTypes(newUiType, oldType, clientsCount);

        int trainingReceipt = 0;
        TrainingReceiptType newReceiptType = null;

        if (newUiType.equals(ST)) {
            if (clientsCount > FULL_TRAINING_LIMIT) {
                trainingReceipt = trainingRepository.getTrainingReceipt(STRETCHING);
                newReceiptType = TrainingReceiptType.STRETCHING;
            } else {
                trainingReceipt = trainingRepository.getTrainingReceipt(STRETCHING_NOT_FULL);
                newReceiptType = TrainingReceiptType.STRETCHING_NOT_FULL;
            }
        } else if (newUiType.equals(PD)) {
            if (clientsCount > FULL_TRAINING_LIMIT) {
                trainingReceipt = trainingRepository.getTrainingReceipt(POLE_DANCE);
                newReceiptType = TrainingReceiptType.POLE_DANCE;
            } else {
                trainingReceipt = trainingRepository.getTrainingReceipt(POLE_DANCE_NOT_FULL);
                newReceiptType = TrainingReceiptType.POLE_DANCE_NOT_FULL;
            }
        } else if (newUiType.equals(IN_2)) {
            trainingReceipt = trainingRepository.getTrainingReceipt(INDIVIDUAL_2);
            newReceiptType = INDIVIDUAL_2;
        } else if (newUiType.equals(IN_3)) {
            trainingReceipt = trainingRepository.getTrainingReceipt(INDIVIDUAL_3);
            newReceiptType = INDIVIDUAL_3;
        }

        training.setReceipts(trainingReceipt);
        training.setTrainingReceiptType(newReceiptType);
        trainingRepository.updateTraining(training);

        for (int clientId: training.getClientIds()) {
            subtractOneTrainingFromSubscription(newReceiptType, clientId);
            addOneTrainingToSubscription(newReceiptType, clientId);
        }
    }

    private void validateTypes(TrainingType newType, TrainingReceiptType oldType, int clientsCount) {
        if ((newType.equals(IN) || newType.equals(IN_2) || newType.equals(IN_3)) &&
                clientsCount > INDIVIDUAL_TRAINING_LIMIT) {
            throw new CanNotChangeTrainingTypeException("Training type can't be changed to individual due to number of clients more than 3");
        }

        if ((newType.equals(IN) || newType.equals(IN_2) || newType.equals(IN_3)) &&
                (oldType.equals(INDIVIDUAL) || oldType.equals(INDIVIDUAL_2) || oldType.equals(INDIVIDUAL_3))) {
            throw new CanNotChangeTrainingTypeException("To change one individual type to another use 'add' or 'remove' client");
        }

        if ((newType.equals(ST) || newType.equals(PD)) && clientsCount < 2) {
            throw new CanNotChangeTrainingTypeException("PD or ST training must have more than 1 client");
        }

        if (newType.equals(IN_2) && clientsCount != 2) {
            throw new CanNotChangeTrainingTypeException("IN_2 training must have 2 clients");
        }

        if (newType.equals(IN_3) && clientsCount != 3) {
            throw new CanNotChangeTrainingTypeException("IN_2 training must have 3 clients");
        }
    }
}
