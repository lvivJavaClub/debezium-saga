package org.coffeejug.cafe.delivery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.coffeejug.cafe.delivery.model.AgentArrivedToKitchen;
import org.coffeejug.cafe.delivery.model.AgentStatus;
import org.coffeejug.cafe.delivery.model.CafeOrder;
import org.coffeejug.cafe.delivery.model.DeliveryAgent;
import org.coffeejug.cafe.delivery.model.DeliveryOrder;
import org.coffeejug.cafe.delivery.model.DeliveryOrderStatus;
import org.coffeejug.cafe.delivery.repository.DeliveryAgentRepository;
import org.coffeejug.cafe.delivery.repository.DeliveryOrderRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryAgentRepository agentRepository;
    private final DeliveryOrderRepository deliveryOrderRepository;
    private final TaskExecutor taskExecutor;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void allocateAgentForDelivery(CafeOrder cafeOrder) {
        DeliveryAgent agent = agentRepository.findAgentForDelivery().orElseThrow();
        log.info("Order will be assigned to agent [orderId={},agentId={}]", agent.getAgentId(), cafeOrder.getOrderId());

        final DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setOrderId(cafeOrder.getOrderId());
        deliveryOrder.setCustomerId(cafeOrder.getCustomerId());
        deliveryOrder.setDeliveryAgent(agent);
        deliveryOrder.setDeliveryStatus(DeliveryOrderStatus.WAITING_PACKAGE);

        requestAgent(agent);
        deliveryOrderRepository.saveAndFlush(deliveryOrder);
    }

    private void requestAgent(DeliveryAgent agent) {
        taskExecutor.execute(() -> {
            agent.setAgentStatus(AgentStatus.ONWAY_TO_KITCHEN);
            agentRepository.save(agent);
            log.info("Agent has been requested [agentId={}]", agent.getAgentId());

            try {
                Thread.sleep(RandomUtils.nextLong(1000, 6000));
                agent.setAgentStatus(AgentStatus.WAITING_FOR_ORDER);
                agentRepository.saveAndFlush(agent);
                applicationEventPublisher.publishEvent(new AgentArrivedToKitchen(agent));
            } catch (InterruptedException e) {
                log.error("Interrupted", e);
                Thread.currentThread().interrupt();
            }
        });
    }

    @Override
    @Transactional
    public void delivereOrder(CafeOrder cafeOrder) {
        var deliveryOrder = deliveryOrderRepository.findById(cafeOrder.getOrderId()).orElseThrow();
        deliveryOrder.setDeliveryStatus(DeliveryOrderStatus.PENDING_DELIVERY);
        if (deliveryOrder.getDeliveryAgent().getAgentStatus() == AgentStatus.WAITING_FOR_ORDER) {
            log.info("Agent is ready to accept order for delivery");
            onwayToCustomer(deliveryOrder.getDeliveryAgent(), deliveryOrder);
        } else {
            log.info("Awaiting agent");
        }
    }

    @EventListener
    @Transactional
    public void onAgentArrived(AgentArrivedToKitchen agentArrivedToKitchen) {
        final DeliveryAgent agent = agentArrivedToKitchen.getAgent();
        log.info("Agent arrived to kitchen [agentId={}]", agent.getAgentId());

        deliveryOrderRepository.findNextPendingOrderForAgent(agent.getAgentId())
            .ifPresent(order -> onwayToCustomer(agent, order));
    }

    private void onwayToCustomer(DeliveryAgent agent, DeliveryOrder order) {
        agent.setAgentStatus(AgentStatus.ONWAY_TO_CUSTOMER);
        order.setDeliveryStatus(DeliveryOrderStatus.ONWAY_TO_CUSTOMER);

        taskExecutor.execute(() -> {
            order.setDeliveryStatus(DeliveryOrderStatus.DELIVERED);
            agent.setAgentStatus(AgentStatus.SLEEPING);
            agentRepository.saveAndFlush(agent);
            deliveryOrderRepository.saveAndFlush(order);
            log.info("order has been delivered [orderId={}]", order.getOrderId());
        });
    }

}
