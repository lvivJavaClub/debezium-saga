package org.coffeejug.cafe.order.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderDeliveryStatus {

    @JsonProperty("order_id")
    private UUID orderId;

    @JsonProperty("status")
    private String kitchenOrderStatus;

}
