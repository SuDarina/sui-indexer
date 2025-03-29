package com.itmo.indexing_service.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigInteger;

@Data
@EqualsAndHashCode(callSuper = false)
public class WrappedObjectContent extends ObjectContent {
    @JsonProperty("inner")
    private String inner;

    @JsonProperty("version")
    private BigInteger version;

}