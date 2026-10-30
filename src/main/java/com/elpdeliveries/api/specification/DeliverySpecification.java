package com.elpdeliveries.api.specification;

import com.elpdeliveries.api.entity.Delivery;
import com.elpdeliveries.api.entity.DeliveryView;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class DeliverySpecification {
    public static Specification<Delivery> hasDlvDate(LocalDate dlvDate) {
        return (root, query, cb) ->
                dlvDate == null ? null :
                        cb.equal(root.get("dlvDate"), dlvDate);
    }

    public static Specification<Delivery> hasFirstName(String firstName) {
        return (root, query, cb) ->
                (firstName == null || firstName.isBlank())
                        ? null
                        : cb.like(
                        cb.lower(root.get("fname")),
                        "%" + firstName.toLowerCase() + "%"
                );
    }

    public static Specification<Delivery> hasLastName(String lastName) {
        return (root, query, cb) ->
                (lastName == null || lastName.isBlank())
                        ? null
                        : cb.like(
                        cb.lower(root.get("lname")),
                        "%" + lastName.toLowerCase() + "%"
                );
    }

    public static Specification<Delivery> hasReceiptId(Integer receiptId) {
        return (root, query, cb) ->
                receiptId == null
                        ? null
                        : cb.equal(root.get("receiptId"), receiptId);
    }

    public static Specification<Delivery> hasCompleted(Boolean completed) {
        return ((root, query, cb) ->
                completed == null
                        ? null
                        : cb.equal(root.get("completed"), completed));
    }

    public static Specification<Delivery> hasView(DeliveryView view) {
        return (root, query, cb) -> {
            if (view == null) {
                return null;
            }
            return switch (view) {
                case ALL -> null;
                case TODAY -> cb.equal(root.get("dlvDate"), LocalDate.now());
                case FUTURE -> cb.greaterThan(root.get("dlvDate"), LocalDate.now());
                case ROUTED -> cb.isNotNull(root.get("routeOrder"));
            };
        };
    }
}
