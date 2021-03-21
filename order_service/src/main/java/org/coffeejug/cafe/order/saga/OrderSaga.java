package org.coffeejug.cafe.order.saga;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.coffeejug.cafe.order.model.Order;
import org.coffeejug.cafe.order.model.OrderCommand;
import org.coffeejug.cafe.order.model.OrderDeliveryStatusConvertor;
import org.coffeejug.cafe.order.model.OrderKitchenStatusConvertor;
import org.coffeejug.cafe.order.model.OrderPaymentStatus;
import org.coffeejug.cafe.order.model.OrderPaymentStatusConvertor;
import org.coffeejug.cafe.order.model.OrderStatus;
import org.coffeejug.cafe.order.service.OrderService;
import org.coffeejug.cafe.saga.model.SagaStep;
import org.coffeejug.cafe.saga.service.SagaInfrastructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderSaga {

    @Autowired
    private OrderService orderService;
    private final ObjectMapper objectMapper;
    private final SagaInfrastructureService sagaInfrastructureService;
    private final OrderPaymentStatusConvertor orderPaymentStatusConvertor;
    private final OrderKitchenStatusConvertor orderKitchenStatusConvertor;
    private final OrderDeliveryStatusConvertor orderDeliveryStatusConvertor;

    public void startOrderSaga(Order order) {
        sagaInfrastructureService.saveStep(
                SagaStep.builder()
                        .id(UUID.randomUUID())
                        .aggregateType("order")
                        .aggregateId(order.getOrderId().toString())
                        .payload(eventPayloadFor(order, "withdrawn-money"))
                        .target("payment")
                        .type("place-order")
                        .build()
        );
    }

    @SneakyThrows
    private String eventPayloadFor(Order order, String action) {
        return objectMapper.writeValueAsString(
                OrderCommand.builder()
                        .orderId(order.getOrderId())
                        .customerId(order.getCustomerId())
                        .currencyCode(order.getCurrencyCode())
                        .totalAmount(order.getTotalPrice())
                        .action(action)
                        .build()
        );
    }


    @KafkaListener(topics = "paymentdb.public.payment_order")
    public void onPaymentCompleted(String message) {
        var paymentMessage = orderPaymentStatusConvertor.getPaymentStatusFromMessage(message);

        if ("PAYMENT_COMPLETED".equals(paymentMessage.getPaymentStatus())) {
            var order = orderService.setOrderAsPayed(paymentMessage.getOrderId());

            sagaInfrastructureService.saveStep(
                    SagaStep.builder()
                            .id(UUID.randomUUID())
                            .aggregateType("order")
                            .aggregateId(order.getOrderId().toString())
                            .payload(eventPayloadFor(order, "start-cooking"))
                            .target("kitchen")
                            .type("start-cooking")
                            .build()
            );

            sagaInfrastructureService.saveStep(
                    SagaStep.builder()
                            .id(UUID.randomUUID())
                            .aggregateType("order")
                            .aggregateId(order.getOrderId().toString())
                            .payload(eventPayloadFor(order, "allocate-agent"))
                            .target("delivery")
                            .type("allocate-agent")
                            .build()
            );

        }

    }

    @KafkaListener(topics = "kitchen.public.kitchen_order")
    public void onKitchenMessage(String message) {
        var kitchenStatus = orderKitchenStatusConvertor.getKitchenStatusFromMessage(message);
        var order = orderService.changeKitchenStatus(kitchenStatus.getOrderId(), kitchenStatus.getKitchenOrderStatus());
        if (order.getOrderStatus() == OrderStatus.KITCHEN_COOKED) {
            sagaInfrastructureService.saveStep(
                    SagaStep.builder()
                            .id(UUID.randomUUID())
                            .aggregateType("order")
                            .aggregateId(order.getOrderId().toString())
                            .payload(eventPayloadFor(order, "deliver-package"))
                            .target("delivery")
                            .type("deliver-package")
                            .build()
            );
        }
    }

    @KafkaListener(topics = "delivery.public.delivery_order")
    public void onDeliveryMessage(String message) {
        var deliveryStatus = orderDeliveryStatusConvertor.getKitchenStatusFromMessage(message);
        orderService.changeDeliveryStatus(deliveryStatus.getOrderId(), deliveryStatus.getKitchenOrderStatus());
    }

}
