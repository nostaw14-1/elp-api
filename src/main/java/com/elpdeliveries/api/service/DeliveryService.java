package com.elpdeliveries.api.service;

import com.elpdeliveries.api.entity.Delivery;
import com.elpdeliveries.api.entity.DeliveryView;
import com.elpdeliveries.api.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.elpdeliveries.api.specification.DeliverySpecification.*;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository repository;
    private final DeliveryGeocodingService geocoder;

    public List<Delivery> getAll() {
        return repository.findAll();
    }

    public Delivery getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery not found: " + id));
    }

    public Delivery create(Delivery delivery) {
        delivery.setId(null);
        try {
            geocoder.geocodeDelivery(delivery);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return repository.save(delivery);
    }

    public Delivery update(Integer id, Delivery updated) throws Exception {
        Delivery existing = getById(id);

        boolean addressChanged =
                !Objects.equals(
                        existing.getAddr(),
                        updated.getAddr());

        existing.setFname(updated.getFname());
        existing.setLname(updated.getLname());
        existing.setAddr(updated.getAddr());
        existing.setPhnum(updated.getPhnum());
        existing.setHomePhone(updated.getHomePhone());
        existing.setReceiptId(updated.getReceiptId());
        existing.setCompleted(updated.getCompleted());
        existing.setHandedToCustomer(updated.getHandedToCustomer());
        existing.setCompletedBy(updated.getCompletedBy());
        existing.setCompletedDate(updated.getCompletedDate());
        existing.setNotes(updated.getNotes());
        existing.setPhoto(updated.getPhoto());
        existing.setIncludedInRoute(updated.getIncludedInRoute());
        existing.setLatitude(updated.getLatitude());
        existing.setLongitude(updated.getLongitude());
        existing.setRouteOrder(updated.getRouteOrder());
        existing.setDlvDate(updated.getDlvDate());
        existing.setIncludedInRoute(updated.getIncludedInRoute());

        if (addressChanged) {
            geocoder.geocodeDelivery(updated);
        }

        return repository.save(existing);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<Delivery> searchDeliveries(
            DeliveryView view,
            LocalDate dlvDate,
            String firstName,
            String surname,
            Integer receiptId,
            Boolean completed
    ) {
       Specification<Delivery> spec = Specification.allOf(
               hasView(view),
               hasDlvDate(dlvDate),
               hasFirstName(firstName),
               hasLastName(surname),
               hasReceiptId(receiptId),
               hasCompleted(completed)
       );
       return repository.findAll(spec);
    }


}
