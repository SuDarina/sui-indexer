package com.itmo.indexing_service.model.transcation.intent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Intent {
    @JsonProperty("scope")
    private int scope;

    @JsonProperty("version")
    private int version;

    @JsonProperty("app_id")
    private int appId;
}
