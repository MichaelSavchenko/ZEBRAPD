package com.zebrapd.usermanagement.controller;

import com.zebrapd.usermanagement.converter.TrainingConverter;
import com.zebrapd.usermanagement.dto.SaveTrainingResponseDto;
import com.zebrapd.usermanagement.dto.TrainingDto;
import com.zebrapd.usermanagement.entity.Training;
import com.zebrapd.usermanagement.entity.TrainingType;
import com.zebrapd.usermanagement.service.TrainingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/training")
public class TrainingController {

    private TrainingService trainingService;
    private TrainingConverter trainingConverter;

    public TrainingController(TrainingService trainingService, TrainingConverter trainingConverter) {
        this.trainingService = trainingService;
        this.trainingConverter = trainingConverter;
    }
    @GetMapping("/{trainingId}")
    public Training training (@PathVariable int trainingId) {
        return trainingService.getTraining(trainingId);
    }

    @PostMapping("/create")
    public SaveTrainingResponseDto createTraining(@RequestBody TrainingDto trainingDto) {
        Training training = trainingConverter.convertToEntity(trainingDto);
        return trainingService.saveTraining(training);
    }

    @PostMapping("/update{trainingId}/addClient{clientId}")
    public void addClient(@PathVariable int trainingId, @PathVariable int clientId) {
        trainingService.addClient(trainingId, clientId);
    }

    @PostMapping("/update{trainingId}/removeClient{clientId}")
    public void removeClient(@PathVariable int trainingId, @PathVariable int clientId) {
        trainingService.removeClient(trainingId, clientId);
    }

    @PostMapping("/update{trainingId}/changeType{newType}")
    public void changeTrainingType(@PathVariable int trainingId, @PathVariable TrainingType newType) {
        trainingService.changeTrainingType(trainingId, newType);
    }


}
