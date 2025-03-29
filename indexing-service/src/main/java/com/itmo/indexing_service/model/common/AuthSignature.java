package com.itmo.indexing_service.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@NoArgsConstructor
public class AuthSignature {
    @JsonProperty("epoch")
    private BigInteger epoch;

    @JsonProperty("signature")
    private String signature;

    @JsonProperty("signers_map")
    private List<Integer> signersMap;
}
