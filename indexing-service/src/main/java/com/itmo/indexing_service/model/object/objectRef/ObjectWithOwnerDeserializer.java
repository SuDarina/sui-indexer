package com.itmo.indexing_service.model.object.objectRef;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.itmo.indexing_service.model.object.owner.Ownership;

import java.io.IOException;

public class ObjectWithOwnerDeserializer extends JsonDeserializer<ObjectWithOwner> {

    @Override
    public ObjectWithOwner deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);

        while (node.isArray() && node.size() == 1) {
            node = node.get(0);
        }

        if (!node.isArray() || node.size() != 2) {
            throw new JsonMappingException(p, "Expected array of length 2 for ObjectWithOwner, got: " + node);
        }

        JsonNode refNode = node.get(0);
        JsonNode ownerNode = node.get(1);

        if (!refNode.isArray() || refNode.size() != 3) {
            throw new JsonMappingException(p, "Expected object ref array of length 3 (id, version, digest), got: " + refNode);
        }

        String objectId = refNode.get(0).asText();
        long version = refNode.get(1).asLong();
        String digest = refNode.get(2).asText();

        ObjectRef ref = new ObjectRef(objectId, version, digest);
        Ownership owner = new Ownership(ownerNode);

        return new ObjectWithOwner(ref, owner);
    }
}
