package com.itmo.indexing_service.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ConsensusCommitPrologueContent extends ObjectContent {
    @JsonProperty("epoch")
    private Long epoch;

    @JsonProperty("round")
    private Long round;

    @JsonProperty("timestamp_ms")
    private Long timestampMs;

}