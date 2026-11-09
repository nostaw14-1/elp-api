package com.elpdeliveries.api.repository;

import com.elpdeliveries.api.entity.RouteDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RouteDeliveryRepository extends JpaRepository<RouteDelivery, Long> {
    @Query("""
    select rd
    from RouteDelivery rd
    join fetch rd.delivery
    where rd.route.id = :routeId
    order by rd.routeOrder
    """)
    List<RouteDelivery> findDeliveriesByRoute(
            Long routeId);
}
