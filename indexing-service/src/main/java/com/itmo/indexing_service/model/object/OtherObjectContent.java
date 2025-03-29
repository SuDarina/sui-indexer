package com.itmo.indexing_service.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.itmo.indexing_service.model.object.ObjectContent;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigInteger;

@Data
@EqualsAndHashCode(callSuper = false)
public class OtherObjectContent extends ObjectContent {
    @JsonProperty("type_id")
    private String typeId;

    @JsonProperty("version")
    private BigInteger version;

    @JsonProperty("contents")
    private String contents;

}