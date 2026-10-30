package com.elpdeliveries.api.repository;

import com.elpdeliveries.api.entity.Delivery;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Integer>, JpaSpecificationExecutor<Delivery> {
    @Modifying
    @Transactional
    @Query("UPDATE Delivery d SET d.routeOrder = null")
    void clearRouteOrders();
}
