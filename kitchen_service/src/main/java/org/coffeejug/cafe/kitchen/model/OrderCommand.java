package org.coffeejug.cafe.kitchen.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderCommand {

    private UUID orderId;
    private UUID customerId;
    private String currencyCode;
    private Long totalAmount;
    private String action;

}
