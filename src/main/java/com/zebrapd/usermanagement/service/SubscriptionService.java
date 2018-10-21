package com.zebrapd.usermanagement.service;

import com.zebrapd.usermanagement.entity.Subscription;
import com.zebrapd.usermanagement.entity.TrainingPriceType;
import com.zebrapd.usermanagement.entity.TrainingType;
import com.zebrapd.usermanagement.error.exception.SubscriptionException;
import com.zebrapd.usermanagement.repositoty.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class SubscriptionService {

    private SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public Subscription getLastSubscriptionByType(int clientId, TrainingType type){
        List<Subscription> activeSubscription = subscriptionRepository.getSubscriptionsByType(clientId, type);
        return activeSubscription.stream()
            .min(Comparator.comparingInt(Subscription::getNumberOfTrainings))
            .orElseThrow(
                ()-> new SubscriptionException(String.format("User '%s' has no subscription of type %s", clientId, type)));
    }

    public List<Subscription> getNotExpiredSubscription(int clientId){
        return subscriptionRepository.getNotExpiredSubscription(clientId);
    }

    public Subscription createSubscription(Subscription subscription){
        Subscription lastSubscription = getLastSubscriptionByType(subscription.getClientId(), subscription.getType());
        int numberOfTrainings = lastSubscription.getNumberOfTrainings();
        if (numberOfTrainings < 0) {
            int newNumberOfTrainings = (subscription.getNumberOfTrainings() + numberOfTrainings);
            subscription.setNumberOfTrainings(newNumberOfTrainings);
        }
        subscription.setDateOfSale(LocalDate.now());
        return subscriptionRepository.createSubscription(subscription);
    }

    public boolean setNumberOfTrainings(int numberOfTrainings, int licenseId){
        return subscriptionRepository.setNumberOfTrainings(numberOfTrainings, licenseId);
    }
}
