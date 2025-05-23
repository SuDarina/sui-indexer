package com.itmo.indexing_service.model.object.owner;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.Iterator;
import java.util.Map;

@Data
public class Ownership {

    private final OwnerType type;
    private final String address;
    private final Long initialSharedVersion;

    @JsonCreator
    public Ownership(JsonNode node) {
        if (!node.isObject() || node.size() != 1) {
            throw new IllegalArgumentException("Invalid ownership object: " + node.toString());
        }

        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        Map.Entry<String, JsonNode> entry = fields.next();
        String ownerType = entry.getKey();
        JsonNode value = entry.getValue();

        switch (ownerType) {
            case "AddressOwner":
                this.type = OwnerType.AddressOwner;
                this.address = value.asText();
                this.initialSharedVersion = null;
                break;
            case "ObjectOwner":
                this.type = OwnerType.ObjectOwner;
                this.address = value.asText();
                this.initialSharedVersion = null;
                break;
            case "Shared":
                this.type = OwnerType.Shared;
                this.address = null;
                this.initialSharedVersion = value.get("initial_shared_version").asLong();
                break;
            case "Immutable":
                this.type = OwnerType.Immutable;
                this.address = null;
                this.initialSharedVersion = null;
                break;
            default:
                throw new IllegalArgumentException("Unknown ownership type: " + node.toString());
        }
    }

}
