package com.elpdeliveries.api.controller;

import com.elpdeliveries.api.dto.GeocodeDto;
import com.elpdeliveries.api.dto.RouteDto;
import com.elpdeliveries.api.dto.RouteRequest;
import com.elpdeliveries.api.dto.RouteResponse;
import com.elpdeliveries.api.entity.Delivery;
import com.elpdeliveries.api.service.NominatimGeocodingService;
import com.elpdeliveries.api.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RouteController {
    private final RouteService routeService;
    private final NominatimGeocodingService geocoder;

    @PostMapping("/routes/generate")
    public ResponseEntity<RouteResponse> generateRoute(@RequestBody RouteRequest request) throws Exception{
            return ResponseEntity.ok(routeService.generateRoute(request.getDeliveryIds(), request.getStaffId()));
    }

    @PostMapping("/geocode")
    public ResponseEntity<GeocodeDto> geocodeAddress(@RequestParam String addr) {
        try {
            return ResponseEntity.ok(geocoder.geocode(addr));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/routes/{id}")
    public void deleteRoute(@PathVariable Long id) {
        routeService.deleteRoute(id);
    }

    @GetMapping("/routes")
    public ResponseEntity<List<RouteDto>> getRoutes() {
        return ResponseEntity.ok(routeService.getRoutes());
    }

    @GetMapping("/routes/{routeId}/deliveries")
    public ResponseEntity<List<Delivery>> searchRouteDeliveries(
            @PathVariable Long routeId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dlvDate,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String receiptId,
            @RequestParam(required = false) Boolean completed
    ) {

        List<Delivery> deliveries = routeService.searchRouteDeliveries(
                routeId,
                dlvDate,
                firstName,
                surname,
                receiptId,
                completed
        );

        return ResponseEntity.ok(deliveries);
    }



}
