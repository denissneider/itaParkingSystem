package com.itaParkingSystem.parkingService;

import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@GrpcService
public class ParkingGrpcService implements ParkingService {

    private final Map<String, ParkingSpot> spots = new ConcurrentHashMap<>();

    public ParkingGrpcService() {
        for (int i = 1; i <= 5; i++) {
            spots.put("spot" + i, ParkingSpot.newBuilder()
                    .setId("spot" + i)
                    .setReserved(false)
                    .setOccupied(false)
                    .build());
        }
    }

    @Override
    public Uni<AvailableSpotsResponse> getAvailableSpots(Empty request) {
        var available = spots.values().stream()
                .filter(spot -> !spot.getReserved() && !spot.getOccupied())
                .toList();

        return Uni.createFrom().item(
                AvailableSpotsResponse.newBuilder().addAllAvailableSpots(available).build()
        );
    }

    @Override
    public Uni<Empty> reserveSpot(ParkingSpotRequest request) {
        return update(request.getSpotId(), spot -> spot.toBuilder().setReserved(true).build());
    }

    @Override
    public Uni<Empty> cancelReservation(ParkingSpotRequest request) {
        return update(request.getSpotId(), spot -> spot.toBuilder().setReserved(false).build());
    }

    @Override
    public Uni<Empty> occupySpot(ParkingSpotRequest request) {
        return update(request.getSpotId(), spot -> spot.toBuilder().setOccupied(true).build());
    }

    @Override
    public Uni<Empty> freeSpot(ParkingSpotRequest request) {
        return update(request.getSpotId(), spot -> spot.toBuilder()
                .setOccupied(false)
                .setReserved(false)
                .build());
    }

    private Uni<Empty> update(String id, java.util.function.Function<ParkingSpot, ParkingSpot> fn) {
        ParkingSpot spot = spots.get(id);
        if (spot != null) {
            spots.put(id, fn.apply(spot));
        }
        return Uni.createFrom().item(Empty.newBuilder().build());
    }

    @Override
    public Uni<AllSpotsResponse> getAllSpots(Empty request) {
        List<ParkingSpot> all = new ArrayList<>(spots.values());
        AllSpotsResponse response = AllSpotsResponse.newBuilder()
                .addAllAllSpots(all)
                .build();
        return Uni.createFrom().item(response);
    }
}