package org.coffeejug.cafe.kitchen.repository;

import org.coffeejug.cafe.kitchen.model.KitchenOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface KitchenOrderRepository extends JpaRepository<KitchenOrder, UUID> {

    Optional<KitchenOrder> findByOrderId(UUID orderId);

}
