package com.itaParkingSystem.parkingService;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "parking_spots")
public class ParkingSpotEntity {

    @Id
    private String id;

    private boolean reserved;

    private boolean occupied;

    // Constructors
    public ParkingSpotEntity() {
    }

    public ParkingSpotEntity(String id, boolean reserved, boolean occupied) {
        this.id = id;
        this.reserved = reserved;
        this.occupied = occupied;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}