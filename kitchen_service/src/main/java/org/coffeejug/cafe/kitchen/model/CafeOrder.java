package org.coffeejug.cafe.kitchen.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@JsonIgnoreProperties
public class CafeOrder {

    @JsonProperty("order_id")
    private UUID orderId;

    @JsonProperty("customer_id")
    private UUID customerId;

    @JsonProperty("currency_code")
    private String currencyCode;

    @JsonProperty("total_price")
    private Long totalAmount;

    @JsonProperty("status")
    private String orderStatus;

}
