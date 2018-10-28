package com.zebrapd.usermanagement.utils;

import com.zebrapd.usermanagement.entity.TrainingReceiptType;
import com.zebrapd.usermanagement.entity.TrainingType;
import com.zebrapd.usermanagement.error.exception.TrainingTypeNotFoundException;

import static com.zebrapd.usermanagement.entity.TrainingReceiptType.*;
import static com.zebrapd.usermanagement.entity.TrainingType.*;

public class TrainingTypeUtils {

    public static TrainingType of(TrainingReceiptType trainingReceiptType) {
        if (trainingReceiptType.equals(POLE_DANCE) || trainingReceiptType.equals(POLE_DANCE_NOT_FULL)) {
            return PD;
        } else if (trainingReceiptType.equals(STRETCHING) || trainingReceiptType.equals(STRETCHING_NOT_FULL)) {
            return ST;
        } else if (trainingReceiptType.equals(INDIVIDUAL)) {
            return IN;
        } else if (trainingReceiptType.equals(INDIVIDUAL_2)) {
            return IN_2;
        } else if (trainingReceiptType.equals(INDIVIDUAL_3)) {
            return IN_3;
        } else {
            throw new TrainingTypeNotFoundException(String.format("Training type is undefined '%s", trainingReceiptType));
        }
    }
}
