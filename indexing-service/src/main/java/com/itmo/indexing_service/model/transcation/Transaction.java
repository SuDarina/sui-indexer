package com.itmo.indexing_service.model.transcation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.itmo.indexing_service.model.object.ObjectData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Transaction {
    @JsonProperty("transaction")
    private TransactionData transaction;

    @JsonProperty("effects")
    private TransactionEffects effects;

    @JsonProperty("events")
    private Object events;

    // TODO заменить Object на ObjectData, либо просто проставить игнор при десериализации
    @JsonProperty("input_objects")
    private List<Object> inputObjects;

    @JsonProperty("output_objects")
    private List<Object> outputObjects;
}