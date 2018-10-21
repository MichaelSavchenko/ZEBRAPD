package com.zebrapd.usermanagement.controller;

import com.zebrapd.usermanagement.dto.SaveTrainingResponseDto;
import com.zebrapd.usermanagement.entity.Training;
import com.zebrapd.usermanagement.entity.TrainingType;
import com.zebrapd.usermanagement.service.PriceService;
import com.zebrapd.usermanagement.service.TrainingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/training")
public class TrainingController {

    private TrainingService trainingService;
    private PriceService priceService;

    public TrainingController(TrainingService trainingService, PriceService priceService) {
        this.trainingService = trainingService;
        this.priceService = priceService;
    }

    @PostMapping("/create")
    public SaveTrainingResponseDto createTraining(@RequestBody Training training) {
        int trainingPrice = priceService.getTrainingPrice(training.getTrainingType(), training.getClientIds().size());
        training.setReceipts(trainingPrice);
        return trainingService.saveTraining(training);
    }

    @PostMapping("/update{trainingId}/addClient{clientId}")
    public void addClient(@PathVariable int trainingId, @PathVariable int clientId, @RequestParam TrainingType type) {
        trainingService.addClient(trainingId, clientId, type);
    }

    @PostMapping("/update{trainingId}/removeClient{clientId}")
    public void removeClient(@PathVariable int trainingId, @PathVariable int clientId, @RequestParam TrainingType type) {
        trainingService.removeClient(trainingId, clientId, type);
    }
}
