package org.coffeejug.cafe.delivery.model;

import org.springframework.context.ApplicationEvent;

public class AgentArrivedToKitchen extends ApplicationEvent {

    public AgentArrivedToKitchen(DeliveryAgent deliveryAgent) {
        super(deliveryAgent);
    }

    public DeliveryAgent getAgent() {
        return DeliveryAgent.class.cast(getSource());
    }
}
