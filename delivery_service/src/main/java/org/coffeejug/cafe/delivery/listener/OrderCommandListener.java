package org.coffeejug.cafe.delivery.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.coffeejug.cafe.delivery.model.CafeOrder;
import org.coffeejug.cafe.delivery.model.OrderCommand;
import org.coffeejug.cafe.delivery.service.DeliveryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Slf4j
@Component
public class OrderCommandListener {

    private final ObjectMapper objectMapper;
    private final DeliveryService deliveryService;

    @KafkaListener(topics = "outbox.event.delivery")
    public void onNewOrderPlaced(String messageBody) throws JsonProcessingException {
        var orderCommand = objectMapper.readValue(messageBody, OrderCommand.class);

        if ("allocate-agent".equals(orderCommand.getAction())) {
            log.info("Creating new delivery order [orderId={}]", orderCommand.getOrderId());
            deliveryService.allocateAgentForDelivery(cafeOrderFromCommand(orderCommand));
        } else if ("deliver-package".equalsIgnoreCase(orderCommand.getAction())) {
            log.info("Package is ready for delivery [orderId={}]", orderCommand.getOrderId());
            deliveryService.delivereOrder(cafeOrderFromCommand(orderCommand));
        }
    }

    private CafeOrder cafeOrderFromCommand(OrderCommand orderCommand) {
        final CafeOrder cafeOrder = new CafeOrder();
        cafeOrder.setOrderId(orderCommand.getOrderId());
        cafeOrder.setCustomerId(orderCommand.getCustomerId());
        cafeOrder.setCurrencyCode(orderCommand.getCurrencyCode());
        cafeOrder.setTotalAmount(orderCommand.getTotalAmount());
        return cafeOrder;
    }

}
