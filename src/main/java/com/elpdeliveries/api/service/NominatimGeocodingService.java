package com.elpdeliveries.api.service;

import com.elpdeliveries.api.dto.GeocodeDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Component
public class NominatimGeocodingService {
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public GeocodeDto geocode(String address) throws IOException, InterruptedException {
        address = address.replaceFirst("(?i)Lindfield Gardens", "2 Ulmarra Pl");
        address = address.replaceFirst(
                "(?i)^unit\\s+([0-9]{1,3}[A-Z]?)\\s*,?\\s*(\\d+)",
                "$1/$2");
        String encoded = URLEncoder.encode(address, StandardCharsets.UTF_8);
        String url =
                "https://nominatim.openstreetmap.org/search" +
                        "?q=" + encoded +
                        "&format=jsonv2" +
                        "&limit=1";

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
                .header("User-Agent", "ElpDeliveries/1.0").GET().build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = mapper.readTree(response.body());
            if (!root.isArray() || root.isEmpty()) {
                return null;
            }
            JsonNode result = root.get(0);
            Double latitude = result.get("lat").asDouble();
            Double longitude = result.get("lon").asDouble();
            return new GeocodeDto(latitude, longitude);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}

