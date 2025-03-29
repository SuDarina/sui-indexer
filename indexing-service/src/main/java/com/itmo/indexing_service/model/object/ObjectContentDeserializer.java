package com.itmo.indexing_service.model.object;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itmo.indexing_service.model.object.move.MoveObjectContent;

import java.io.IOException;

public class ObjectContentDeserializer extends JsonDeserializer<ObjectContent> {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public ObjectContent deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        if (node.has("Move")) {
            return mapper.treeToValue(node.get("Move"), MoveObjectContent.class);
        } else if (node.has("Package")) {
            return mapper.treeToValue(node.get("Package"), PackageObjectContent.class);
        } else if (node.has("Coin")) {
            return mapper.treeToValue(node.get("Coin"), CoinObjectContent.class);
        } else if (node.has("ConsensusCommitPrologue")) {
            return mapper.treeToValue(node.get("ConsensusCommitPrologue"), ConsensusCommitPrologueContent.class);
        } else if (node.has("Other")) {
            return mapper.treeToValue(node.get("Other"), OtherObjectContent.class);
        } else if (node.has("Genesis")) {
            return mapper.treeToValue(node.get("Genesis"), GenesisObjectContent.class);
        } else if (node.has("Wrapped")) {
            return mapper.treeToValue(node.get("Wrapped"), WrappedObjectContent.class);
        }

        throw new IllegalArgumentException("Unknown object type: " + node);
    }
}