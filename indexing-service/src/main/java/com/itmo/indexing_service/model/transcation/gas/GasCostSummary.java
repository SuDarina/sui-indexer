package com.itmo.indexing_service.model.transcation.gas;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GasCostSummary {

    @JsonProperty("computationCost")
    String computationCost;

    @JsonProperty("storageCost")
    String storageCost;

    @JsonProperty("storageRebate")
    String storageRebate;

    @JsonProperty("nonRefundableStorageFee")
    String nonRefundableStorageFee;

}
