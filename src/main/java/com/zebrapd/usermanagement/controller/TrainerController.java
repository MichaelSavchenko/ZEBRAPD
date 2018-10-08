package com.zebrapd.usermanagement.controller;

import com.zebrapd.usermanagement.entity.Trainer;
import com.zebrapd.usermanagement.service.TrainerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/trainer")
public class TrainerController {

    private TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @GetMapping("/getTrainerById/{entityId}")
    public Trainer getTrainerById(@PathVariable int entityId){
        return trainerService.getTrainerById(entityId);
    }

    @GetMapping("/getTrainerByEmail/{email}")
    public Trainer getTrainerByEmail(@PathVariable String email){
        return trainerService.getTrainerByEmail(email);
    }

    @GetMapping("/getTrainerByPhone/{phoneNumber}")
    public Trainer getTrainerByPhone(@PathVariable String phoneNumber){
        return trainerService.getTrainerByEmail(phoneNumber);
    }

    @GetMapping("/getAll")
    public List<Trainer> getAllTrainers(){
        return trainerService.getAllTrainers();
    }

    @GetMapping("/getAllInactive")
    public List<Trainer> getAllInactiveTrainers(){
        return trainerService.getAllInactiveTrainers();
    }

    @GetMapping("/getAllActive")
    public List<Trainer> getAllActiveTrainers(){
        return trainerService.getAllActiveTrainers();
    }

    @PostMapping("/create")
    public Trainer create (@RequestBody Trainer trainer){
        return trainerService.createTrainer(trainer);
    }

    @PostMapping("/update")
    public boolean update (@RequestBody Trainer trainer){
        return trainerService.updateTrainer(trainer);
    }
}
