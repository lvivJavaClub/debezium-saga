package org.coffeejug.cafe.kitchen.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.coffeejug.cafe.kitchen.model.CafeOrder;
import org.coffeejug.cafe.kitchen.model.KitchenOrder;
import org.coffeejug.cafe.kitchen.model.KitchenOrderStatus;
import org.coffeejug.cafe.kitchen.repository.KitchenOrderRepository;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class KitchenServiceImpl implements KitchenService {

    private final KitchenOrderRepository kitchenOrderRepository;
    private final TaskExecutor taskExecutor;

    @Override
    public void placeOrder(CafeOrder cafeOrder) {
        if (kitchenOrderRepository.findByOrderId(cafeOrder.getOrderId()).isPresent()) {
            log.debug("Order has been already placed [orderId={}]", cafeOrder.getOrderId());
            return;
        }

        final KitchenOrder kitchenOrder = new KitchenOrder();
        kitchenOrder.setKitchenOrderId(UUID.randomUUID());
        kitchenOrder.setOrderId(cafeOrder.getOrderId());
        kitchenOrder.setOrderStatus(KitchenOrderStatus.ORDER_PLACED);
        kitchenOrderRepository.save(kitchenOrder);

        log.info("Order has been placed [orderId={},kitchenOrderId={}",
                kitchenOrder.getOrderId(), kitchenOrder.getKitchenOrderId());

        taskExecutor.execute(() -> prepareOrder(kitchenOrder));
    }

    @SneakyThrows
    private void prepareOrder(KitchenOrder kitchenOrder) {
        Thread.sleep(1000L);

        log.info("Start cooking [orderId={}]", kitchenOrder.getOrderId());
        kitchenOrder.setOrderStatus(KitchenOrderStatus.ORDER_COOKING);
        kitchenOrderRepository.saveAndFlush(kitchenOrder);

        Thread.sleep(4000L);
        log.info("Complete cooking [orderId={}]", kitchenOrder.getOrderId());
        kitchenOrder.setOrderStatus(KitchenOrderStatus.ORDER_COOKED);
        kitchenOrderRepository.saveAndFlush(kitchenOrder);
    }
}
