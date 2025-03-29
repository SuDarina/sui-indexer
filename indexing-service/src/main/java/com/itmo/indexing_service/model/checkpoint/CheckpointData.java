package com.itmo.indexing_service.model.checkpoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.itmo.indexing_service.model.transcation.Transaction;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckpointData {
    @JsonProperty("checkpoint_summary")
    private CheckpointSummary checkpointSummary;

    @JsonProperty("checkpoint_contents")
    private CheckpointContents checkpointContents;

    @JsonProperty("transactions")
    private List<Transaction> transactions;
}