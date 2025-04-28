package com.itaParkingSystem.parkingService;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ParkingSpotInitializer {

    @Inject
    ParkingSpotRepository parkingSpotRepository;

    @PostConstruct
    void init() {
        if (parkingSpotRepository.count() == 0) {
            for (int i = 1; i <= 5; i++) {
                ParkingSpotEntity spot = new ParkingSpotEntity("spot" + i, false, false);
                parkingSpotRepository.persist(spot);
            }
        }
    }
}