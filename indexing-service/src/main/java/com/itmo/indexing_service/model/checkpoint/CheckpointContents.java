package com.itmo.indexing_service.model.checkpoint;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CheckpointContents {
    @JsonProperty("V1")
    private CheckpointContentsV1 v1;
}