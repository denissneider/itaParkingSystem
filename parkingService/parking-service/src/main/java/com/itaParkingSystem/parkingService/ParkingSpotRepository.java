package com.itaParkingSystem.parkingService;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ParkingSpotRepository implements PanacheRepositoryBase<ParkingSpotEntity, String> {
    // Panache will generate basic methods automatically
}