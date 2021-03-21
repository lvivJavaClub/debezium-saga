package org.coffeejug.cafe.kitchen.model;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CafeOrderConvertor {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public CafeOrder getOrderFromMessage(String message) {
        try (final JsonParser parser = objectMapper.createParser(message)) {
            TreeNode treeNode = parser.readValueAsTree();
            return getOrderFromSnapshot(treeNode.get("after"));
        }
    }

    private CafeOrder getOrderFromSnapshot(TreeNode after) {
        return objectMapper.convertValue(after, CafeOrder.class);
    }

}
