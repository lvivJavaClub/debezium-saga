package org.coffeejug.cafe.kitchen.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.coffeejug.cafe.kitchen.model.CafeOrder;
import org.coffeejug.cafe.kitchen.model.CafeOrderConvertor;
import org.coffeejug.cafe.kitchen.model.OrderCommand;
import org.coffeejug.cafe.kitchen.service.KitchenService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CafeOrderCommandListener {

    private final ObjectMapper objectMapper;
    private final CafeOrderConvertor cafeOrderConvertor;
    private final KitchenService kitchenService;


    @KafkaListener(topics = "outbox.event.kitchen")
    public void onNewOrderCreated(String message) throws JsonProcessingException {
        var orderCommand = objectMapper.readValue(message, OrderCommand.class);
        if ("start-cooking".equals(orderCommand.getAction())) {
            log.info("Process new order");
            kitchenService.placeOrder(cafeOrderFromCommand(orderCommand));
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
