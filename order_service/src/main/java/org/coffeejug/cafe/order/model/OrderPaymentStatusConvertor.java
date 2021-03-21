package org.coffeejug.cafe.order.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderPaymentStatusConvertor {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public OrderPaymentStatus getPaymentStatusFromMessage(String message) {
        try (final JsonParser parser = objectMapper.createParser(message)) {
            TreeNode treeNode = parser.readValueAsTree();
            return getPaymentStatusFromSnapshot(treeNode.get("after"));
        }
    }

    private OrderPaymentStatus getPaymentStatusFromSnapshot(TreeNode after) {
        return objectMapper.convertValue(after, OrderPaymentStatus.class);
    }

}
