package com.zebrapd.usermanagement.service;

import com.zebrapd.usermanagement.entity.Subscription;
import com.zebrapd.usermanagement.entity.TrainingReceiptType;
import com.zebrapd.usermanagement.entity.TrainingType;
import com.zebrapd.usermanagement.error.exception.SubscriptionException;
import com.zebrapd.usermanagement.repositoty.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    private SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public Optional<Subscription> getActiveSubscription(int clientId, TrainingType trainingType) {
        List<Subscription> subscriptions = subscriptionRepository.getActiveSubscriptions(clientId, trainingType);
        if (subscriptions.size() == 0) {
           return Optional.empty();
        }

        return subscriptions.stream()
                .min(Comparator.comparingInt(Subscription::getNumberOfTrainings));
    }

    public List<Subscription> getNotExpiredSubscriptions(int clientId) {
        return subscriptionRepository.getNotExpiredSubscription(clientId);
    }

    public Subscription createSubscription(Subscription subscription) {
        Optional<Subscription> lastSubscription = getActiveSubscription(subscription.getClientId(), subscription.getType());
        if (lastSubscription.isPresent()) {
            int numberOfTrainings = lastSubscription.get().getNumberOfTrainings();
            if (numberOfTrainings < 0) {
                int newNumberOfTrainings = (subscription.getNumberOfTrainings() + numberOfTrainings);
                subscription.setNumberOfTrainings(newNumberOfTrainings);
            }
        }
        subscription.setDateOfSale(LocalDate.now());
        return subscriptionRepository.createSubscription(subscription);
    }

    boolean setNumberOfTrainings(int numberOfTrainings, int licenseId) {
        return subscriptionRepository.setNumberOfTrainings(numberOfTrainings, licenseId);
    }
}
