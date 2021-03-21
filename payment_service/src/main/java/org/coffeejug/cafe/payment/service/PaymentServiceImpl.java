package org.coffeejug.cafe.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.coffeejug.cafe.payment.repository.OrderPaymentRepository;
import org.coffeejug.cafe.payment.model.CafeOrder;
import org.coffeejug.cafe.payment.model.OrderPayment;
import org.coffeejug.cafe.payment.model.PaymentStatus;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final OrderPaymentRepository orderPaymentRepository;

    private final TaskExecutor taskExecutor;

    @Override
    public OrderPayment withdrawnMoney(CafeOrder cafeOrder) {
        var orderPaymentByOrderId = orderPaymentRepository.findOrderPaymentByOrderId(cafeOrder.getOrderId());
        if (orderPaymentByOrderId.isPresent()) {
            log.info("Order has been already accepted by payment [orderId={}]", cafeOrder.getOrderId());
            return orderPaymentByOrderId.get();
        }

        log.info("Withdrawn Money [orderId={},customerId={}]", cafeOrder.getOrderId(), cafeOrder.getCustomerId());
        var orderPayment = createPaymentOrderFor(cafeOrder);

        log.info("Create pending payment [orderId={},paymentId={}]", cafeOrder.getOrderId(), orderPayment.getPaymentId());
        orderPaymentRepository.saveAndFlush(orderPayment);
        taskExecutor.execute(() -> completePayment(orderPayment));

        return orderPayment;
    }

    private OrderPayment createPaymentOrderFor(CafeOrder cafeOrder) {
        return OrderPayment.builder()
                .paymentId(UUID.randomUUID())
                .orderId(cafeOrder.getOrderId())
                .customerId(cafeOrder.getCustomerId())
                .currencyCode(cafeOrder.getCurrencyCode())
                .amount(cafeOrder.getTotalAmount())
                .paymentStatus(PaymentStatus.PAYMENT_PENDING)
                .build();
    }

    private void completePayment(OrderPayment orderPayment) {
        if (Objects.equals(orderPayment.getCurrencyCode(), "USD")) {
            orderPayment.setPaymentStatus(PaymentStatus.PAYMENT_COMPLETED);
        } else {
            orderPayment.setPaymentStatus(PaymentStatus.PAYMENT_FAILED);
        }
        log.info("Payment completed [orderId={},paymentId={},status={}]", orderPayment.getOrderId(),
                orderPayment.getPaymentId(), orderPayment.getPaymentStatus());
        orderPaymentRepository.saveAndFlush(orderPayment);
    }
}
