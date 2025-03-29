package com.itmo.indexing_service.model.object.move;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class MoveTypeDeserializer extends JsonDeserializer<MoveType> {
    @Override
    public MoveType deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);

        if (node.isTextual()) {
            // Если type_ — это строка (например, "GasCoin")
            return new StringMoveType(node.asText());
        } else if (node.isObject()) {
            // Если type_ — это объект (например, {"Other": {...}})
            String typeName = node.fieldNames().next(); // Получаем имя типа (например, "Other")
            JsonNode typeData = node.get(typeName);

            return switch (typeName) {
                case "Other" -> p.getCodec().treeToValue(typeData, OtherMoveType.class);
                case "Vector" -> p.getCodec().treeToValue(typeData, VectorMoveType.class);
                case "Struct" -> p.getCodec().treeToValue(typeData, StructMoveType.class);
                default -> throw new IllegalArgumentException("Unknown type: " + typeName);
            };
        }

        throw new IllegalArgumentException("Invalid type format");
    }
}