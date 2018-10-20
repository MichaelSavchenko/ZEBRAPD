package com.zebrapd.usermanagement.controller;

import com.zebrapd.usermanagement.dto.SaveTrainingResponseDto;
import com.zebrapd.usermanagement.entity.Training;
import com.zebrapd.usermanagement.service.PriceService;
import com.zebrapd.usermanagement.service.TrainingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public SaveTrainingResponseDto createTraining(@RequestBody Training training){
        int trainingPrice = priceService.getTrainingPrice(training.getTrainingType(), training.getClientIds().size());
        training.setReceipts(trainingPrice);
        return trainingService.saveTraining(training);
    }
}
