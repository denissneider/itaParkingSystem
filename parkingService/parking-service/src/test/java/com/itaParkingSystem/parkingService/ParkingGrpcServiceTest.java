package com.itaParkingSystem.parkingService;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ParkingGrpcServiceTest {

    @GrpcClient
    ParkingServiceGrpc.ParkingServiceBlockingStub parkingService;

    @BeforeEach
    @Transactional
    void setup() {
        // Clean up DB between tests
        ParkingSpotRepository repo = new ParkingSpotRepository();
        repo.deleteAll();
    }

    @Test
    void testAddSpot() {
        ParkingSpot spot = ParkingSpot.newBuilder()
                .setId("test-spot-1")
                .setReserved(false)
                .setOccupied(false)
                .build();

        parkingService.addSpot(spot);

        var response = parkingService.getAllSpots(Empty.newBuilder().build());
        assertEquals(1, response.getAllSpotsCount());
        assertEquals("test-spot-1", response.getAllSpots(0).getId());
    }

    @Test
    void testReserveSpot() {
        addTestSpot("spot-1");

        parkingService.reserveSpot(ParkingSpotRequest.newBuilder().setSpotId("spot-1").build());

        var response = parkingService.getAllSpots(Empty.newBuilder().build());
        assertTrue(response.getAllSpots(0).getReserved());
    }

    @Test
    void testOccupyAndFreeSpot() {
        addTestSpot("spot-2");

        parkingService.occupySpot(ParkingSpotRequest.newBuilder().setSpotId("spot-2").build());
        var occupied = parkingService.getAllSpots(Empty.newBuilder().build());
        assertTrue(occupied.getAllSpots(0).getOccupied());

        parkingService.freeSpot(ParkingSpotRequest.newBuilder().setSpotId("spot-2").build());
        var freed = parkingService.getAllSpots(Empty.newBuilder().build());
        assertFalse(freed.getAllSpots(0).getOccupied());
        assertFalse(freed.getAllSpots(0).getReserved());
    }

    @Test
    void testRemoveSpot() {
        addTestSpot("spot-3");

        parkingService.removeSpot(SpotIdRequest.newBuilder().setSpotId("spot-3").build());

        var response = parkingService.getAllSpots(Empty.newBuilder().build());
        assertEquals(0, response.getAllSpotsCount());
    }

    @Test
    void testGetAvailableSpots() {
        addTestSpot("spot-4");
        addTestSpot("spot-5");

        parkingService.reserveSpot(ParkingSpotRequest.newBuilder().setSpotId("spot-4").build());

        var response = parkingService.getAvailableSpots(Empty.newBuilder().build());
        assertEquals(1, response.getAvailableSpotsCount());
        assertEquals("spot-5", response.getAvailableSpots(0).getId());
    }

    private void addTestSpot(String spotId) {
        ParkingSpot spot = ParkingSpot.newBuilder()
                .setId(spotId)
                .setReserved(false)
                .setOccupied(false)
                .build();
        parkingService.addSpot(spot);
    }
}