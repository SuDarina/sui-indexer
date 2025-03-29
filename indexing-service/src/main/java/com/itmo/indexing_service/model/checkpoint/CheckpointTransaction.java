package com.itmo.indexing_service.model.checkpoint;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CheckpointTransaction {
    @JsonProperty("transaction")
    private String transaction;

    @JsonProperty("effects")
    private String effects;
}