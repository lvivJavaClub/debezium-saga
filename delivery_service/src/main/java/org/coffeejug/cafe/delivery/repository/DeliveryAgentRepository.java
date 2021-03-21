package org.coffeejug.cafe.delivery.repository;

import org.coffeejug.cafe.delivery.model.DeliveryAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryAgentRepository extends JpaRepository<DeliveryAgent, UUID> {


    @Query(
            value = "SELECT * "
                    + " FROM delivery_agent"
                    + " ORDER BY CASE WHEN status = 'SLEEPING' THEN 1 ELSE 2 END ASC"
                    + " LIMIT 1",
            nativeQuery = true
    )
    Optional<DeliveryAgent> findAgentForDelivery();

}
