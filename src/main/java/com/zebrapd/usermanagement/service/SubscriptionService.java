package com.zebrapd.usermanagement.service;

import com.zebrapd.usermanagement.entity.Subscription;
import com.zebrapd.usermanagement.entity.TrainingType;
import com.zebrapd.usermanagement.error.exception.SubscriptionException;
import com.zebrapd.usermanagement.repositoty.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class SubscriptionService {

    private SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public Subscription getCurrentSubscriptionByType(int clientId, TrainingType type){
        List<Subscription> activeSubscription = subscriptionRepository.getActiveSubscriptionByType(clientId, type);
        return activeSubscription.stream()
            .min(Comparator.comparingInt(Subscription::getNumberOfTrainings))
            .orElseThrow(
                ()-> new SubscriptionException(String.format("Usser '%s' has no active subscription of type %s", clientId, type)));
    }

    public List<Subscription> getNotExpiredSubscription(int clientId){
        return subscriptionRepository.getNotExpiredSubscription(clientId);
    }

    //todo check debt get number of debt training
    public Subscription createSubscription(Subscription subscription){
        return subscriptionRepository.createSubscription(subscription);
    }

    public boolean setNumberOftrainings (int numberOfTrainings, int licenseId){
        return subscriptionRepository.setNumberOfTrainings(numberOfTrainings, licenseId);
    }
}

//todo create training in 2 steps: 1 - check subscriptions (and create new if all are expired) 2 - create training
