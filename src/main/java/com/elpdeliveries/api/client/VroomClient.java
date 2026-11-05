package com.elpdeliveries.api.client;

import com.elpdeliveries.api.entity.Delivery;
import com.elpdeliveries.api.entity.Settings;
import com.elpdeliveries.api.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VroomClient {
    private final SettingsService settingsService;
    private final HttpClient client = HttpClient.newHttpClient();

    public List<Integer> optimiseRoute(List<Delivery> deliveries) throws Exception {
        Settings settings = settingsService.getSettings();
        if (deliveries.isEmpty()) {
            throw new RuntimeException("No deliveries to order!");
        }
        String requestJson = VroomJsonUtil.buildRequest(deliveries, settings.getDepotLat(), settings.getDepotLong());
        String responseJson = postToVroom(requestJson);
        return VroomJsonUtil.parse(responseJson);
    }

    private String postToVroom(String requestJson) throws Exception {
        Settings settings = settingsService.getSettings();
        String VROOM_URL = "http://" + settings.getVroomIp() + ":3000";
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(VROOM_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("VROOM request failed. HTTP " + response.statusCode()
                    + "\n" + response.body());
        }

        return response.body();
    }
}
