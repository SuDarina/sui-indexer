package com.itmo.indexing_service.model.checkpoint;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.itmo.indexing_service.model.transcation.gas.GasCostSummary;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@NoArgsConstructor
public class CheckpointSummaryData {
    @JsonProperty("epoch")
    private BigInteger epoch;

    @JsonProperty("sequence_number")
    private BigInteger sequenceNumber;

    @JsonProperty("network_total_transactions")
    private BigInteger networkTotalTransactions;

    @JsonProperty("content_digest")
    private String contentDigest;

    @JsonProperty("previous_digest")
    private String previousDigest;

    @JsonProperty("epoch_rolling_gas_cost_summary")
    private GasCostSummary epochRollingGasCostSummary;

    @JsonProperty("timestamp_ms")
    private BigInteger timestampMs;

    @JsonProperty("checkpoint_commitments")
    private List<String> checkpointCommitments;

    @JsonProperty("end_of_epoch_data")
    private Object endOfEpochData; // Может быть null или другой структурой

    @JsonProperty("version_specific_data")
    private List<String> versionSpecificData;
}
