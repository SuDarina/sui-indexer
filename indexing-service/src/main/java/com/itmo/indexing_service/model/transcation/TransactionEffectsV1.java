package com.itmo.indexing_service.model.transcation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itmo.indexing_service.model.transcation.gas.GasCostSummary;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@NoArgsConstructor
public class TransactionEffectsV1 {
    @JsonProperty("status")
    @JsonDeserialize(using = TransactionStatusDeserializer.class)
    private TransactionStatus status;

    @JsonProperty("executed_epoch")
    private BigInteger executedEpoch;

    @JsonProperty("gas_used")
    private GasCostSummary gasUsed;

    @JsonProperty("modified_at_versions")
    private List<List<String>> modifiedAtVersions;

    @JsonProperty("shared_objects")
    private List<List<String>> sharedObjects;

    @JsonProperty("transaction_digest")
    private String transactionDigest;

    @JsonProperty("created")
    private List<Object> created;

    @JsonProperty("mutated")
    private List<List<Object>> mutated;

    @JsonProperty("unwrapped")
    private List<Object> unwrapped;

    @JsonProperty("deleted")
    private List<Object> deleted;

    @JsonProperty("unwrapped_then_deleted")
    private List<Object> unwrappedThenDeleted;

    @JsonProperty("wrapped")
    private List<Object> wrapped;

    @JsonProperty("gas_object")
    private List<Object> gasObject;

    @JsonProperty("events_digest")
    private String eventsDigest;

    @JsonProperty("dependencies")
    private List<String> dependencies;
}