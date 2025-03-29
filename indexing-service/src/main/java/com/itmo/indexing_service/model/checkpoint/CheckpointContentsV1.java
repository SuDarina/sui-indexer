package com.itmo.indexing_service.model.checkpoint;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CheckpointContentsV1 {
    @JsonProperty("transactions")
    private List<CheckpointTransaction> transactions;

    @JsonProperty("user_signatures")
    private List<List<String>> userSignatures;
}