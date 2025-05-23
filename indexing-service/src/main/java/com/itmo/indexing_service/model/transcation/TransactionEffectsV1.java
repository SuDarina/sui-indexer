package com.itmo.indexing_service.model.transcation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itmo.indexing_service.model.object.objectRef.ObjectWithOwner;
import com.itmo.indexing_service.model.transcation.gas.GasCostSummary;
import com.itmo.indexing_service.model.object.objectRef.ObjectWithOwnerListDeserializer;
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
    @JsonDeserialize(using = ObjectWithOwnerListDeserializer.class)
    private List<ObjectWithOwner> created;

    @JsonProperty("mutated")
    @JsonDeserialize(using = ObjectWithOwnerListDeserializer.class)
    private List<List<ObjectWithOwner>> mutated;

    @JsonProperty("unwrapped")
    @JsonDeserialize(using = ObjectWithOwnerListDeserializer.class)
    private List<ObjectWithOwner> unwrapped;

    @JsonProperty("deleted")
    @JsonDeserialize(using = ObjectWithOwnerListDeserializer.class)
    private List<ObjectWithOwner> deleted;

    @JsonProperty("unwrapped_then_deleted")
    @JsonDeserialize(using = ObjectWithOwnerListDeserializer.class)
    private List<ObjectWithOwner> unwrappedThenDeleted;

    @JsonProperty("wrapped")
    @JsonDeserialize(using = ObjectWithOwnerListDeserializer.class)
    private List<ObjectWithOwner> wrapped;

    @JsonProperty("gas_object")
    @JsonDeserialize(using = ObjectWithOwnerListDeserializer.class)
    private List<ObjectWithOwner> gasObject;

    @JsonProperty("events_digest")
    private String eventsDigest;

    @JsonProperty("dependencies")
    private List<String> dependencies;
}