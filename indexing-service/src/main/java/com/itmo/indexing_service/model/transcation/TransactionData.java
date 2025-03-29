package com.itmo.indexing_service.model.transcation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class TransactionData {
    @JsonProperty("data")
    private List<TransactionDataItem> data;

    @JsonProperty("auth_signature")
    private Map<String, Object> authSignature; // Может быть пустым или содержать данные
}