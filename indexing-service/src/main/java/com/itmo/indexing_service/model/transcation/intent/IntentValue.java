package com.itmo.indexing_service.model.transcation.intent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IntentValue {
    @JsonProperty("V1")
    private IntentValueV1 v1;
}
