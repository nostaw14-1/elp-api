package com.elpdeliveries.api.controller;

import com.elpdeliveries.api.entity.Delivery;
import com.elpdeliveries.api.entity.DeliveryView;
import com.elpdeliveries.api.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService service;

    @GetMapping
    public ResponseEntity<List<Delivery>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Delivery> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<Delivery> create(@RequestBody Delivery delivery) {
        return ResponseEntity.ok(service.create(delivery));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Delivery> update(@PathVariable Integer id, @RequestBody Delivery delivery) throws Exception {
        return ResponseEntity.ok(service.update(id, delivery));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
        ResponseEntity.ok();
    }

    @GetMapping("/search")
    public List<Delivery> searchDeliveries(
            @RequestParam(required = false) DeliveryView view,
            @RequestParam(required = false) LocalDate dlvDate,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Integer receiptId,
            @RequestParam(required = false) Boolean completed) {

        return service.searchDeliveries(
                view,
                dlvDate,
                firstName,
                lastName,
                receiptId,
                completed
        );
    }
}
