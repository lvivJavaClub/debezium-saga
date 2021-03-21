package org.coffeejug.cafe.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.coffeejug.cafe.order.model.Order;
import org.coffeejug.cafe.order.model.OrderStatus;
import org.coffeejug.cafe.order.repository.OrderRepository;
import org.coffeejug.cafe.order.saga.OrderSaga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    @Autowired
    OrderSaga orderSaga;

    @Override
    @Transactional
    public Order createOrder(Order source) {
        log.info("Create new order [orderId={},customerId={}]", source.getOrderId(), source.getCustomerId());
        source.setTotalPrice(calculateTotal(source));
        source.getOrderItems().forEach(item -> item.setOrder(source));
        var order = orderRepository.saveAndFlush(source);
        orderSaga.startOrderSaga(order);
        return order;
    }

    @Override
    @Transactional
    public Order setOrderAsPayed(UUID orderId) {
        log.info("Change order status to PAYMENT_COMPLETED [orderId={}]", orderId);
        var order = orderRepository.findById(orderId).orElseThrow();
        order.setOrderStatus(OrderStatus.PAYMENT_COMPLETED);
        return order;
    }

    @Override
    @Transactional
    public Order changeKitchenStatus(UUID orderId, String kitchenOrderStatus) {
        var order = orderRepository.findById(orderId).orElseThrow();
        log.info("Change order status by message from kitchen [orderId={},currentStatus={},kitchenStatus={}]",
                order.getOrderId(), order.getOrderStatus(), kitchenOrderStatus);
        if ("ORDER_PLACED".equals(kitchenOrderStatus) && order.getOrderStatus() == OrderStatus.PAYMENT_COMPLETED) {
            order.setOrderStatus(OrderStatus.KITCHEN_PENDING);
        } else if ("ORDER_COOKING".equals(kitchenOrderStatus) && order.getOrderStatus() == OrderStatus.KITCHEN_PENDING) {
            order.setOrderStatus(OrderStatus.KITCHEN_COOKING);
        } else if ("ORDER_COOKED".equals(kitchenOrderStatus) && order.getOrderStatus() == OrderStatus.KITCHEN_COOKING) {
            order.setOrderStatus(OrderStatus.KITCHEN_COOKED);
        }
        return order;
    }

    @Override
    @Transactional
    public void changeDeliveryStatus(UUID orderId, String deliveryStatus) {
        var order = orderRepository.findById(orderId).orElseThrow();
        log.info("Change order status by message from delivery [orderId={},currentStatus={},deliveryStatus={}]",
                order.getOrderId(), order.getOrderStatus(), deliveryStatus);
        if ("PENDING_DELIVERY".equals(deliveryStatus)) {
            order.setOrderStatus(OrderStatus.DELIVERY_WAITING);
        } else if ("ONWAY_TO_CUSTOMER".equals(deliveryStatus)) {
            order.setOrderStatus(OrderStatus.DELIVERY_ONWAY);
        } else if ("DELIVERED".equals(deliveryStatus)) {
            order.setOrderStatus(OrderStatus.DELIVERY_COMPLETED);
        }
    }

    private long calculateTotal(Order source) {
        return source.getOrderItems().stream()
                .mapToLong(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

}
