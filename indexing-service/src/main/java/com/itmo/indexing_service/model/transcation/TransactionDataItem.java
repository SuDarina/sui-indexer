package com.itmo.indexing_service.model.transcation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.itmo.indexing_service.model.transcation.intent.IntentMessage;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TransactionDataItem {
    @JsonProperty("intent_message")
    private IntentMessage intentMessage;

    @JsonProperty("tx_signatures")
    private List<String> txSignatures;
}