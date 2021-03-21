package org.coffeejug.cafe.order.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.coffeejug.cafe.order.model.OrderPaymentStatusConvertor;
import org.coffeejug.cafe.order.service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
//@Component
public class PaymentListener {

    private final OrderService orderService;
    private final OrderPaymentStatusConvertor orderPaymentStatusConvertor;

    @KafkaListener(topics = "paymentdb.public.payment_order")
    public void onPaymentCompleted(String message) {
        log.info("Process payment message");
        var paymentStatus = orderPaymentStatusConvertor.getPaymentStatusFromMessage(message);

        if ("PAYMENT_COMPLETED".equalsIgnoreCase(paymentStatus.getPaymentStatus())) {
            orderService.setOrderAsPayed(paymentStatus.getOrderId());
        }
    }

}
