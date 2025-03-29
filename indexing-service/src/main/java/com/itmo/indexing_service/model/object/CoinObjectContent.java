package com.itmo.indexing_service.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigInteger;

@Data
@Deprecated
@EqualsAndHashCode(callSuper = false)
public class CoinObjectContent extends ObjectContent {
    @JsonProperty("balance")
    private String balance;

    @JsonProperty("id")
    private String id;

    @JsonProperty("version")
    private BigInteger version;

}