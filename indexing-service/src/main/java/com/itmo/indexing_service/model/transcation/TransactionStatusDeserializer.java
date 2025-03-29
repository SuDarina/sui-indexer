package com.itmo.indexing_service.model.transcation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class TransactionStatusDeserializer extends JsonDeserializer<TransactionStatus> {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public TransactionStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        if (node.isTextual()) {
            // Простой строковый статус "Success"
            if ("Success".equals(node.asText())) {
                return new TransactionSuccess();
            }
        } else if (node.isObject() && node.has("Failure")) {
            // Объект с ошибкой {"Failure":{"error":"...","command":0}}
            JsonNode failureNode = node.get("Failure");
            return mapper.treeToValue(failureNode, TransactionFailure.class);
        }

        throw new IllegalArgumentException("Unknown transaction status format: " + node);
    }
}