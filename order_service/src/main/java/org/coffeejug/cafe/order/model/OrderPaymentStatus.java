package org.coffeejug.cafe.order.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderPaymentStatus {

    @JsonProperty("order_id")
    private UUID orderId;

    @JsonProperty("payment_status")
    private String paymentStatus;

}
