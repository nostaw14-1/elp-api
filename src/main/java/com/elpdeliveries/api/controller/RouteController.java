package com.elpdeliveries.api.controller;

import com.elpdeliveries.api.dto.GeocodeDto;
import com.elpdeliveries.api.dto.RouteRequest;
import com.elpdeliveries.api.service.NominatimGeocodingService;
import com.elpdeliveries.api.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RouteController {
    private final RouteService routeService;
    private final NominatimGeocodingService geocoder;

    @PostMapping("/routes/generate")
    public ResponseEntity<List<Integer>> generateRoute(@RequestBody RouteRequest request) throws Exception{
            return ResponseEntity.ok(routeService.generateRoute(request.getDeliveryIds()));
    }

    @PostMapping("/geocode")
    public ResponseEntity<GeocodeDto> geocodeAddress(@RequestParam String addr) {
        try {
            return ResponseEntity.ok(geocoder.geocode(addr));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
