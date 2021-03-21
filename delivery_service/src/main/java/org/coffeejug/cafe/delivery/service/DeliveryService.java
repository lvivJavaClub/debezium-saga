package org.coffeejug.cafe.delivery.service;

import org.coffeejug.cafe.delivery.model.CafeOrder;

public interface DeliveryService {

    void allocateAgentForDelivery(CafeOrder cafeOrder);

    void delivereOrder(CafeOrder cafeOrder);

}
