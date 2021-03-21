package org.coffeejug.cafe.order.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.coffeejug.cafe.order.model.Order;
import org.coffeejug.cafe.order.model.OrderItem;
import org.coffeejug.cafe.order.model.OrderStatus;
import org.coffeejug.cafe.order.service.OrderService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public HttpEntity<Order> placeNewOrder(@RequestParam(defaultValue = "USD") String currencyCode) {
        log.info("Place new order");
        Order order = Order.builder()
                .orderId(UUID.randomUUID())
                .orderStatus(OrderStatus.PAYMENT_PENDING)
                .currencyCode(currencyCode)
                .customerId(UUID.randomUUID())
                .orderItems(
                        List.of(
                                OrderItem.builder()
                                        .itemId(UUID.randomUUID())
                                        .productSku("abc001")
                                        .quantity(RandomUtils.nextInt(1,5))
                                        .price(RandomUtils.nextLong(10, 10_000))
                                        .build(),
                                OrderItem.builder()
                                        .itemId(UUID.randomUUID())
                                        .productSku("abc007")
                                        .quantity(RandomUtils.nextInt(1,5))
                                        .price(RandomUtils.nextLong(10, 10_000))
                                        .build()
                        )
                )
                .build();

        return ResponseEntity.ok(orderService.createOrder(order));
    }

}
