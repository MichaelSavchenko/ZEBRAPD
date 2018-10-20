package com.zebrapd.usermanagement.controller;

import com.zebrapd.usermanagement.dto.TrainingPriceDto;
import com.zebrapd.usermanagement.entity.TrainingType;
import com.zebrapd.usermanagement.service.PriceService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/price")
public class PriceController {

    private PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping
    public int getTrainingPriceForAdmin(@RequestParam TrainingType type){
        return priceService.getTrainingPriceForAdmin(type);
    }

    @PostMapping
    public void setTrainingPrice(@RequestBody TrainingPriceDto dto) {
        priceService.setTrainingPrice(dto.getTrainingType(), dto.getPrice());
    }
}
