package com.elpdeliveries.api.service;

import com.elpdeliveries.api.client.VroomClient;
import com.elpdeliveries.api.entity.Delivery;
import com.elpdeliveries.api.repository.DeliveryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class RouteService {
    private final DeliveryRepository repository;
    private final VroomClient vroomClient;

    public RouteService(DeliveryRepository repository, VroomClient vroomClient) {
        this.repository = repository;
        this.vroomClient = vroomClient;
    }

    @Transactional
    public List<Integer> generateRoute(List<Integer> deliveryIds) throws Exception {
        repository.clearRouteOrders();
        List<Delivery> deliveries = repository.findAllById(deliveryIds);
        deliveries.removeIf(delivery -> !delivery.getIncludedInRoute());
        Map<Integer, Delivery> deliveryMap =
                deliveries.stream()
                        .collect(Collectors.toMap(
                                Delivery::getId,
                                Function.identity()));
        List<Integer> orderedIds = vroomClient.optimiseRoute(deliveries);
        int routeOrder = 1;
        for (Integer id : orderedIds) {
            Delivery delivery = deliveryMap.get(id);
            delivery.setRouteOrder(routeOrder++);
        }
        repository.saveAll(deliveries);
        return orderedIds;
    }
}
