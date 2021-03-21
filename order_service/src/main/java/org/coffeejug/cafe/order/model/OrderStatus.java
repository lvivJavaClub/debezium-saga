package org.coffeejug.cafe.order.model;

public enum OrderStatus {

    PAYMENT_PENDING,
    PAYMENT_COMPLETED,

    KITCHEN_PENDING,
    KITCHEN_COOKING,
    KITCHEN_COOKED,

    DELIVERY_WAITING,
    DELIVERY_ONWAY,
    DELIVERY_COMPLETED

}
