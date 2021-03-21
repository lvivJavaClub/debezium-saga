package org.coffeejug.cafe.delivery.repository;

import org.coffeejug.cafe.delivery.model.DeliveryOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, UUID> {

    @Query(
            value = "SELECT *"
                    + "  FROM delivery_order"
                    + "  WHERE agent_ref = :agentId AND"
                    + "        status = 'PENDING_DELIVERY'"
                    + "  LIMIT 1",
            nativeQuery = true
    )
    Optional<DeliveryOrder> findNextPendingOrderForAgent(@Param("agentId") UUID agentId);

}
