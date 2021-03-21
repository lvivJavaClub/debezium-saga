package org.coffeejug.cafe.order.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderKitchenStatusConvertor {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public OrderKitchenStatus getKitchenStatusFromMessage(String message) {
        try (final JsonParser parser = objectMapper.createParser(message)) {
            TreeNode treeNode = parser.readValueAsTree();
            return getKitchenStatusFromSnapshot(treeNode.get("after"));
        }
    }

    private OrderKitchenStatus getKitchenStatusFromSnapshot(TreeNode after) {
        return objectMapper.convertValue(after, OrderKitchenStatus.class);
    }

}
