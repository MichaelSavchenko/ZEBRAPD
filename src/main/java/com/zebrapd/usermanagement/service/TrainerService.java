package com.zebrapd.usermanagement.service;

import com.zebrapd.usermanagement.entity.Trainer;
import com.zebrapd.usermanagement.error.exception.ValidationException;
import com.zebrapd.usermanagement.repositoty.TrainerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TrainerService {

    private TrainerRepository trainerRepository;

    public TrainerService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    public Trainer getTrainerById(int entityId){
        return trainerRepository.getTrainerById(entityId);
    }

    public Trainer getTrainerByEmail(String email){
        return trainerRepository.getTrainerByEmail(email);
    }

    public Trainer getTrainerByPhone(String phoneNumber){
        return trainerRepository.getTrainerByPhone(phoneNumber);
    }

    public List<Trainer> getAllTrainers(){
        return trainerRepository.getAllTrainers();
    }

    public List<Trainer> getAllActiveTrainers(){
        return trainerRepository.getAllActiveTrainers();
    }

    public List<Trainer> getAllInactiveTrainers(){
        return trainerRepository.getAllInactiveTrainers();
    }

    public Trainer createTrainer(Trainer trainer){
        validateEmail(trainer.getEmail());
        validatePhoneNumber(trainer.getPhoneNumber());
        return trainerRepository.createTrainer(trainer);
    }
//todo validation of phone and mail
    public boolean updateTrainer(Trainer trainer){
        try {
            return trainerRepository.updateTrainer(trainer);
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        Trainer trainerByPhone = trainerRepository.getTrainerByPhone(phoneNumber);
        if (Objects.nonNull(trainerByPhone)){
            throw new ValidationException("Trainer with such phone number already exists: " + phoneNumber);
        }
    }

    private void validateEmail(String email) {
        Trainer trainerByEmail = trainerRepository.getTrainerByEmail(email);
        if (Objects.nonNull(trainerByEmail)) {
            throw new ValidationException("Client with such email already exists: " + email);
        }
    }
}
