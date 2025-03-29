package com.itmo.indexing_service.model.transcation.intent;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.itmo.indexing_service.model.transcation.gas.GasData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class IntentValueV1 {
    @JsonProperty("kind")
    private Map<String, Object> kind;

    @JsonProperty("sender")
    private String sender;

    @JsonProperty("gas_data")
    private GasData gasData;

    @JsonProperty("expiration")
    private String expiration;
}