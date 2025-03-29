package com.itmo.indexing_service.model.object.move;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itmo.indexing_service.model.object.ObjectContent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MoveObjectContent extends ObjectContent {
    @JsonProperty("type_")
    @JsonDeserialize(using = MoveTypeDeserializer.class)
    private MoveType type;

    @JsonProperty("has_public_transfer")
    private boolean hasPublicTransfer;

    @JsonProperty("version")
    private BigInteger version;

    @JsonProperty("contents")
    private List<Integer> contents;
}