package com.elpdeliveries.api.service;

import com.elpdeliveries.api.dto.GeocodeDto;
import com.elpdeliveries.api.entity.Delivery;
import org.springframework.stereotype.Component;

@Component
public class DeliveryGeocodingService {
    private final NominatimGeocodingService geocoder = new NominatimGeocodingService();

    public void geocodeDelivery(Delivery delivery) throws Exception {
        GeocodeDto result = geocoder.geocode(delivery.getAddr());
        if (result == null) {
            throw new RuntimeException("Could not find address!");
        }
        delivery.setLatitude(result.getLatitude());
        delivery.setLongitude(result.getLongitude());
    }
}
