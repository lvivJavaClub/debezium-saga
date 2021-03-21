package org.coffeejug.cafe.delivery.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.coffeejug.cafe.delivery.model.CafeOrder;
import org.coffeejug.cafe.delivery.model.CafeOrderConvertor;
import org.coffeejug.cafe.delivery.service.DeliveryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderListener {

    private final CafeOrderConvertor cafeOrderConvertor;
    private final DeliveryService deliveryService;

    @KafkaListener(topics = "orderdb.public.cafe_order")
    public void onNewOrderCreated(String message) {
        final CafeOrder cafeOrder = cafeOrderConvertor.getOrderFromMessage(message);
        if ("PAYMENT_COMPLETED".equalsIgnoreCase(cafeOrder.getOrderStatus())) {
            log.info("Creating new delivery order [orderId={}]", cafeOrder.getOrderId());
            deliveryService.allocateAgentForDelivery(cafeOrder);
        } else if ("KITCHEN_COOKED".equalsIgnoreCase(cafeOrder.getOrderStatus())) {
            log.info("Package is ready for delivery [orderId={}]", cafeOrder.getOrderId());
            deliveryService.delivereOrder(cafeOrder);
        }
    }
}
