package com.itmo.indexing_service.model.checkpoint;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.itmo.indexing_service.model.common.AuthSignature;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CheckpointSummary {
    @JsonProperty("data")
    private CheckpointSummaryData data;

    @JsonProperty("auth_signature")
    private AuthSignature authSignature;
}
