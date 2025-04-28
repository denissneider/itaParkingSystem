package com.itaParkingSystem.parkingService;

import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import java.util.*;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import io.smallrye.common.annotation.Blocking;

@GrpcService
@Blocking
public class ParkingGrpcService implements ParkingService {

    @Inject
    ParkingSpotRepository parkingSpotRepository;

    @Override
    public Uni<AvailableSpotsResponse> getAvailableSpots(Empty request) {
        List<ParkingSpotEntity> available = parkingSpotRepository.listAll()
                .stream()
                .filter(spot -> !spot.isReserved() && !spot.isOccupied())
                .toList();

        List<ParkingSpot> spots = available.stream()
                .map(spot -> ParkingSpot.newBuilder()
                        .setId(spot.getId())
                        .setReserved(spot.isReserved())
                        .setOccupied(spot.isOccupied())
                        .build())
                .toList();

        return Uni.createFrom().item(
                AvailableSpotsResponse.newBuilder()
                        .addAllAvailableSpots(spots)
                        .build()
        );
    }

    @Override
    @Transactional
    public Uni<Empty> reserveSpot(ParkingSpotRequest request) {
        ParkingSpotEntity spot = parkingSpotRepository.findById(request.getSpotId());
        if (spot != null) {
            spot.setReserved(true);
            parkingSpotRepository.persist(spot);
        }
        return Uni.createFrom().item(Empty.newBuilder().build());
    }

    @Override
    @Transactional
    public Uni<Empty> cancelReservation(ParkingSpotRequest request) {
        ParkingSpotEntity spot = parkingSpotRepository.findById(request.getSpotId());
        if (spot != null) {
            spot.setReserved(false);
            parkingSpotRepository.persist(spot);
        }
        return Uni.createFrom().item(Empty.newBuilder().build());
    }

    @Override
    @Transactional
    public Uni<Empty> occupySpot(ParkingSpotRequest request) {
        ParkingSpotEntity spot = parkingSpotRepository.findById(request.getSpotId());
        if (spot != null) {
            spot.setOccupied(true);
            parkingSpotRepository.persist(spot);
        }
        return Uni.createFrom().item(Empty.newBuilder().build());
    }

    @Override
    @Transactional
    public Uni<Empty> freeSpot(ParkingSpotRequest request) {
        ParkingSpotEntity spot = parkingSpotRepository.findById(request.getSpotId());
        if (spot != null) {
            spot.setOccupied(false);
            spot.setReserved(false);
            parkingSpotRepository.persist(spot);
        }
        return Uni.createFrom().item(Empty.newBuilder().build());
    }

    @Override
    @Transactional
    public Uni<Empty> addSpot(ParkingSpot request) {
        ParkingSpotEntity newSpot = new ParkingSpotEntity(request.getId(), request.getReserved(), request.getOccupied());
        parkingSpotRepository.persist(newSpot);
        return Uni.createFrom().item(Empty.newBuilder().build());
    }

    @Override
    @Transactional
    public Uni<Empty> removeSpot(SpotIdRequest request) {
        ParkingSpotEntity spot = parkingSpotRepository.findById(request.getSpotId());
        if (spot != null) {
            parkingSpotRepository.delete(spot);
        }
        return Uni.createFrom().item(Empty.newBuilder().build());
    }

    @Override
    public Uni<AllSpotsResponse> getAllSpots(Empty request) {
        List<ParkingSpotEntity> allSpots = parkingSpotRepository.listAll();
        List<ParkingSpot> spots = allSpots.stream()
                .map(spot -> ParkingSpot.newBuilder()
                        .setId(spot.getId())
                        .setReserved(spot.isReserved())
                        .setOccupied(spot.isOccupied())
                        .build())
                .toList();

        AllSpotsResponse response = AllSpotsResponse.newBuilder()
                .addAllAllSpots(spots)
                .build();
        return Uni.createFrom().item(response);
    }
}