package com.elpdeliveries.api.service;

import com.elpdeliveries.api.client.VroomClient;
import com.elpdeliveries.api.entity.Delivery;
import com.elpdeliveries.api.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RouteService {
    private final DeliveryRepository repository;
    private final VroomClient vroomClient;

    public RouteService(DeliveryRepository repository, VroomClient vroomClient) {
        this.repository = repository;
        this.vroomClient = vroomClient;
    }

    public List<Integer> generateRoute(List<Integer> deliveryIds) throws Exception {
        List<Delivery> deliveries = repository.findAllById(deliveryIds);
        deliveries.removeIf(delivery -> !delivery.getIncludedInRoute());
        return vroomClient.optimiseRoute(deliveries);
    }
}
