package org.coffeejug.cafe.payment.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.coffeejug.cafe.payment.model.CafeOrder;
import org.coffeejug.cafe.payment.model.OrderCommand;
import org.coffeejug.cafe.payment.service.PaymentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class OrderCommandListener {

    private final ObjectMapper objectMapper;
    private final PaymentService paymentService;

    @KafkaListener(topics = "outbox.event.payment")
    public void onNewOrderPlaced(String messageBody) throws JsonProcessingException {
        var orderCommand = objectMapper.readValue(messageBody, OrderCommand.class);
        paymentService.withdrawnMoney(cafeOrderFromCommand(orderCommand));
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
