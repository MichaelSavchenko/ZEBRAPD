package com.zebrapd.usermanagement.dto;

import java.util.ArrayList;
import java.util.List;

public class SaveTrainingResponseDto {
    private List<String> lastTrainingLeft;
    private List<String> noMoreTraining;

    public SaveTrainingResponseDto() {
        this.lastTrainingLeft = new ArrayList<>();
        this.noMoreTraining = new ArrayList<>();
    }

    public void addToLastTrainingLeft(String lastName){
        lastTrainingLeft.add(lastName);
    }

    public void addNomoreTrainings(String lastName) {
        noMoreTraining.add(lastName);
    }

    public List<String> getLastTrainingLeft() {
        return lastTrainingLeft;
    }

    public List<String> getNoMoreTraining() {
        return noMoreTraining;
    }
}
