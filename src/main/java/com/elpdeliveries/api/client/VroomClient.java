package com.elpdeliveries.api.client;

import com.elpdeliveries.api.config.VroomProperties;
import com.elpdeliveries.api.entity.Delivery;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
public class VroomClient {

    private final VroomProperties properties;
    private final HttpClient client = HttpClient.newHttpClient();
    public VroomClient(VroomProperties properties) {
        this.properties = properties;
    }

    public List<Integer> optimiseRoute(List<Delivery> deliveries) throws Exception {
        if (deliveries.isEmpty()) {
            throw new RuntimeException("No deliveries to order!");
        }
        String requestJson = VroomJsonUtil.buildRequest(deliveries, properties.getLatitude(), properties.getLongitude());
        String responseJson = postToVroom(requestJson);
        return VroomJsonUtil.parse(responseJson);
    }

    private String postToVroom(String requestJson) throws Exception {
        String VROOM_URL = properties.getUrl();
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
