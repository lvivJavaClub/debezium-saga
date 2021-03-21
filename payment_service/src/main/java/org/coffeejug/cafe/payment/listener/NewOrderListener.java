package org.coffeejug.cafe.payment.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.coffeejug.cafe.payment.model.CafeOrder;
import org.coffeejug.cafe.payment.model.CafeOrderConvertor;
import org.coffeejug.cafe.payment.service.PaymentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NewOrderListener {

    final CafeOrderConvertor cafeOrderConvertor;
    final PaymentService paymentService;

    @KafkaListener(topics = "orderdb.public.cafe_order")
    public void onNewOrderCreated(String message) {
        final CafeOrder orderFromMessage = cafeOrderConvertor.getOrderFromMessage(message);
        if ("PAYMENT_PENDING".equalsIgnoreCase(orderFromMessage.getOrderStatus())) {
            log.info("Process new order");
            paymentService.withdrawnMoney(orderFromMessage);
        }
    }

}
