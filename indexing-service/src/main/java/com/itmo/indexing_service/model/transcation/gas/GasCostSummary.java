package com.itmo.indexing_service.model.transcation.gas;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonDeserialize(using = GasCostSummaryDeserializer.class)
public class GasCostSummary {

    @JsonProperty("computationCost")
    Long computationCost;

    @JsonProperty("storageCost")
    Long storageCost;

    @JsonProperty("storageRebate")
    Long storageRebate;

    @JsonProperty("nonRefundableStorageFee")
    Long nonRefundableStorageFee;

}
