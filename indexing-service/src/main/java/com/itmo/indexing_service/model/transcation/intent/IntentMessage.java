package com.itmo.indexing_service.model.transcation.intent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IntentMessage {
    @JsonProperty("intent")
    private Intent intent;

    @JsonProperty("value")
    private IntentValue value;
}