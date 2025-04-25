package com.itmo.indexing_service.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itmo.indexing_service.model.object.owner.Ownership;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Map;

@Data
@NoArgsConstructor
public class ObjectData {

    @JsonProperty("data")
    @JsonDeserialize(using = ObjectContentDeserializer.class)
    private ObjectContent data;

    @JsonProperty("owner")
    private Ownership owner;

    @JsonProperty("previous_transaction")
    private String previousTransaction;

    @JsonProperty("storage_rebate")
    private BigInteger storageRebate;
}