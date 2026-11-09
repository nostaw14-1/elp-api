package com.elpdeliveries.api.service;

import com.elpdeliveries.api.client.VroomClient;
import com.elpdeliveries.api.dto.RouteDto;
import com.elpdeliveries.api.dto.RouteResponse;
import com.elpdeliveries.api.dto.StaffDto;
import com.elpdeliveries.api.entity.*;
import com.elpdeliveries.api.repository.DeliveryRepository;
import com.elpdeliveries.api.repository.RouteDeliveryRepository;
import com.elpdeliveries.api.repository.RouteRepository;
import com.elpdeliveries.api.repository.ZRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class RouteService {
    private final DeliveryRepository repository;
    private final VroomClient vroomClient;
    private final ZRepository zRepository;
    private final RouteRepository routeRepository;
    private final RouteDeliveryRepository routeDeliveryRepository;


    @Transactional
    public RouteResponse generateRoute(List<Integer> deliveryIds, String staffId) throws Exception {
        if (deliveryIds == null || deliveryIds.isEmpty()) {
            throw new RuntimeException("No deliveries selected");
        }
        StaffDto staff = zRepository.getStaffById(staffId);
        if (staff == null) {
            throw new RuntimeException("Staff member not found");
        }
        List<Delivery> deliveries = repository.findAllById(deliveryIds);
        deliveries.removeIf(delivery -> !delivery.getIncludedInRoute());
        if (deliveries.isEmpty()) {
            throw new RuntimeException("No deliveries found");
        }
        List<Integer> orderedIds = vroomClient.optimiseRoute(deliveries);
        Route route = new Route();
        route.setCreatedAt(LocalDateTime.now());
        route.setStaffId(staff.id());
        route.setStaffName(staff.toString());
        route.setRouteStatus("Uncompleted");
        route = routeRepository.save(route);

        Map<Integer, Integer> routeOrderLookup = new HashMap<>();
        for (int i = 0; i < orderedIds.size(); i++) {
            routeOrderLookup.put(orderedIds.get(i), i + 1);
        }

        for (Delivery delivery : deliveries) {
            Integer routeOrder = routeOrderLookup.get(delivery.getId());
            delivery.setRouteOrder(routeOrder);
            RouteDelivery routeDelivery = new RouteDelivery();
            routeDelivery.setRoute(route);
            routeDelivery.setDelivery(delivery);
            routeDelivery.setRouteOrder(routeOrder);
            route.getRouteDeliveries().add(routeDelivery);
        }
        repository.saveAll(deliveries);
        routeRepository.save(route);

        return new RouteResponse(
                route.getId(),
                route.getStaffName(),
                orderedIds.size(),
                route.getCreatedAt()
        );
    }

    @Transactional
    public void deleteRoute(Long routeId) {
        Route route = routeRepository.findById(routeId).orElseThrow(() -> new RuntimeException("Route not found"));
        for (RouteDelivery routeDelivery: route.getRouteDeliveries()) {
            Delivery delivery = routeDelivery.getDelivery();
            delivery.setRouteOrder(null);
            repository.save(delivery);
        }
        routeRepository.delete(route);
    }

    public List<RouteDto> getRoutes() {
        return routeRepository.findAll().stream()
                .map(route -> new RouteDto(
                    route.getId(),
                    route.getStaffName(),
                    route.getCreatedAt(),
                    route.getRouteStatus(),
                    route.getRouteDeliveries().size()
                )).toList();
    }

    public List<Delivery> searchRouteDeliveries(
            Long routeId,
            LocalDate dlvDate,
            String firstName,
            String surname,
            String receiptId,
            Boolean completed
    ) {

        return routeDeliveryRepository.findDeliveriesByRoute(routeId)
                .stream()
                .map(rd -> {
                    Delivery d = rd.getDelivery();
                    d.setRouteOrder(rd.getRouteOrder());
                    return d;
                })
                .filter(d -> dlvDate == null ||
                        dlvDate.equals(d.getDlvDate()))
                .filter(d -> firstName == null ||
                        d.getFname().toLowerCase().contains(firstName.toLowerCase()))
                .filter(d -> surname == null ||
                        d.getLname().toLowerCase().contains(surname.toLowerCase()))
                .filter(d -> receiptId == null ||
                        d.getReceiptId().toLowerCase().contains(receiptId.toLowerCase()))
                .filter(d -> completed == null ||
                        d.getCompleted() == completed)
                .toList();
    }
}
