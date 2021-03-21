package org.coffeejug.cafe.order.service;

import org.coffeejug.cafe.order.model.Order;

import java.util.UUID;

public interface OrderService {

    Order createOrder(Order source);

    Order setOrderAsPayed(UUID orderId);

    Order changeKitchenStatus(UUID orderId, String kitchenOrderStatus);

    void changeDeliveryStatus(UUID orderId, String kitchenOrderStatus);
}
