package com.itmo.indexing_service.model.object.objectRef;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.itmo.indexing_service.model.object.owner.Ownership;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObjectWithOwnerListDeserializer extends JsonDeserializer<List<ObjectWithOwner>> {

    @Override
    public List<ObjectWithOwner> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode rootNode = p.getCodec().readTree(p);
        List<ObjectWithOwner> result = new ArrayList<>();

        parseNestedArray(rootNode, result);
        return result;
    }

    private void parseNestedArray(JsonNode node, List<ObjectWithOwner> result) {
        if (!node.isArray()) return;

        for (JsonNode item : node) {
            if (isObjectWithOwnerNode(item)) {
                result.add(parseObjectWithOwner(item));
            } else if (item.isArray()) {
                parseNestedArray(item, result);  // рекурсивно ищем дальше
            }
        }
    }

    private boolean isObjectWithOwnerNode(JsonNode node) {
        return node.isArray() && node.size() == 2
                && node.get(0).isArray() && node.get(0).size() == 3
                && node.get(1).isObject();
    }

    private ObjectWithOwner parseObjectWithOwner(JsonNode node) {
        JsonNode refNode = node.get(0);
        JsonNode ownerNode = node.get(1);

        String objectId = refNode.get(0).asText();
        long version = refNode.get(1).asLong();
        String digest = refNode.get(2).asText();

        ObjectRef ref = new ObjectRef(objectId, version, digest);
        Ownership owner = new Ownership(ownerNode);

        return new ObjectWithOwner(ref, owner);
    }
}
