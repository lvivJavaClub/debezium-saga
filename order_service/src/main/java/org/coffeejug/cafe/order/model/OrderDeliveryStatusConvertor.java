package org.coffeejug.cafe.order.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderDeliveryStatusConvertor {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public OrderDeliveryStatus getKitchenStatusFromMessage(String message) {
        try (final JsonParser parser = objectMapper.createParser(message)) {
            TreeNode treeNode = parser.readValueAsTree();
            return getDeliveryStatusFromSnapshot(treeNode.get("after"));
        }
    }

    private OrderDeliveryStatus getDeliveryStatusFromSnapshot(TreeNode after) {
        return objectMapper.convertValue(after, OrderDeliveryStatus.class);
    }

}
