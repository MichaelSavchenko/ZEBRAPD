package com.zebrapd.usermanagement.service;

import com.zebrapd.usermanagement.entity.License;
import com.zebrapd.usermanagement.entity.TrainingType;
import com.zebrapd.usermanagement.error.exception.LicenseException;
import com.zebrapd.usermanagement.repositoty.LicenseRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class LicenseService {

    private LicenseRepository licenseRepository;

    public LicenseService(LicenseRepository licenseRepository) {
        this.licenseRepository = licenseRepository;
    }

    public License getCurrentLicense(int userId, TrainingType type){
        List<License> activeLicenses = licenseRepository.getActiveLicense(userId, type);
        return activeLicenses.stream()
            .min(Comparator.comparingInt(License::getNumberOfTrainings))
            .orElseThrow(
                ()-> new LicenseException(String.format("Usser '%s' has no active license of type %s", userId, type)));
    }
}
