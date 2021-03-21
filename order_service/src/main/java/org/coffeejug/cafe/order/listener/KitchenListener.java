package org.coffeejug.cafe.order.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.coffeejug.cafe.order.model.OrderKitchenStatusConvertor;
import org.coffeejug.cafe.order.service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
//@Component
public class KitchenListener {

    private final OrderKitchenStatusConvertor orderKitchenStatusConvertor;
    private final OrderService orderService;

    @KafkaListener(topics = "kitchen.public.kitchen_order")
    public void onKitchenMessage(String message) {
        var kitchenStatus = orderKitchenStatusConvertor.getKitchenStatusFromMessage(message);
        orderService.changeKitchenStatus(kitchenStatus.getOrderId(), kitchenStatus.getKitchenOrderStatus());
    }
}
