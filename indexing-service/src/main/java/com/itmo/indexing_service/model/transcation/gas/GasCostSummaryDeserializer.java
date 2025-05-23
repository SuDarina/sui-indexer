package com.itmo.indexing_service.model.transcation.gas;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class GasCostSummaryDeserializer extends JsonDeserializer<GasCostSummary> {

    @Override
    public GasCostSummary deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        GasCostSummary summary = new GasCostSummary();
        summary.setComputationCost(parseLong(node.get("computationCost")));
        summary.setStorageCost(parseLong(node.get("storageCost")));
        summary.setStorageRebate(parseLong(node.get("storageRebate")));
        summary.setNonRefundableStorageFee(parseLong(node.get("nonRefundableStorageFee")));

        return summary;
    }

    private Long parseLong(JsonNode node) {
        if (node == null || node.isNull()) return 0L;
        try {
            return new java.math.BigInteger(node.asText()).longValue();
        } catch (Exception e) {
            return 0L;
        }
    }
}

