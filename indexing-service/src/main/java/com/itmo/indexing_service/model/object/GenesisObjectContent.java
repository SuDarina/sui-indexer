package com.itmo.indexing_service.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class GenesisObjectContent extends ObjectContent {
    @JsonProperty("objects")
    private List<String> objects;

}