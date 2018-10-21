package com.zebrapd.usermanagement.controller;

import com.zebrapd.usermanagement.entity.Subscription;
import com.zebrapd.usermanagement.entity.TrainingPriceType;
import com.zebrapd.usermanagement.entity.TrainingType;
import com.zebrapd.usermanagement.service.SubscriptionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/license")
public class SubscriptionController {

    private SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/create")
    public Subscription createSubscription(@RequestBody Subscription subscription){
        return subscriptionService.createSubscription(subscription);
    }

    @GetMapping("/getCurrentSubscriptionByType/{clientId}/{trainingType}")
    public Subscription getCurrentSubscriptionByType(@PathVariable int clientId, @PathVariable TrainingType trainingType){
        return subscriptionService.getLastSubscriptionByType(clientId, trainingType);
    }

    @GetMapping("/getNotExpiredLicense/{clientId}")
    public List<Subscription> getNotExpiredSubscription(@PathVariable int clientId){
        return subscriptionService.getNotExpiredSubscription(clientId);
    }


}
