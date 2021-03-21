package org.coffeejug.cafe.kitchen.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.coffeejug.cafe.kitchen.model.CafeOrder;
import org.coffeejug.cafe.kitchen.model.CafeOrderConvertor;
import org.coffeejug.cafe.kitchen.service.KitchenService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CafeOrderListener {

    private final CafeOrderConvertor cafeOrderConvertor;
    private final KitchenService kitchenService;

    @KafkaListener(topics = "orderdb.public.cafe_order")
    public void onNewOrderCreated(String message) {
        final CafeOrder cafeOrder = cafeOrderConvertor.getOrderFromMessage(message);
        if ("PAYMENT_COMPLETED".equalsIgnoreCase(cafeOrder.getOrderStatus())) {
            log.info("Process new order");
            kitchenService.placeOrder(cafeOrder);
        }
    }

}
