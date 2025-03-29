package com.itmo.indexing_service.model.transcation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionEffects {
    @JsonProperty("V1")
    private TransactionEffectsV1 v1;
}