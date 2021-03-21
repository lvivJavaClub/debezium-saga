package org.coffeejug.cafe.order.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.coffeejug.cafe.order.model.OrderDeliveryStatusConvertor;
import org.coffeejug.cafe.order.service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
//@Component
public class DeliveryListener {

    private final OrderDeliveryStatusConvertor orderDeliveryStatusConvertor;
    private final OrderService orderService;

    @KafkaListener(topics = "delivery.public.delivery_order")
    public void onKitchenMessage(String message) {
        var deliveryStatus = orderDeliveryStatusConvertor.getKitchenStatusFromMessage(message);
        orderService.changeDeliveryStatus(deliveryStatus.getOrderId(), deliveryStatus.getKitchenOrderStatus());
    }


}
