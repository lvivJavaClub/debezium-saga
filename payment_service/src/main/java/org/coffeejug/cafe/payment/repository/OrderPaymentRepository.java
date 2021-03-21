package org.coffeejug.cafe.payment.repository;

import org.coffeejug.cafe.payment.model.OrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderPaymentRepository extends JpaRepository<OrderPayment, UUID> {

    Optional<OrderPayment> findOrderPaymentByOrderId(UUID orderId);

}
