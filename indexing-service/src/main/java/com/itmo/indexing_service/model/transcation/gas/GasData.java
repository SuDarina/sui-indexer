package com.itmo.indexing_service.model.transcation.gas;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@NoArgsConstructor
public class GasData {
    @JsonProperty("payment")
    private List<List<String>> payment;

    @JsonProperty("owner")
    private String owner;

    @JsonProperty("price")
    private Long price;

    @JsonProperty("budget")
    private Long budget;
}
