package com.itmo.indexing_service.model.transcation;

import com.fasterxml.jackson.annotation.JsonSubTypes;

@JsonSubTypes({
        @JsonSubTypes.Type(value = TransactionSuccess.class, name = "Success"),
        @JsonSubTypes.Type(value = TransactionFailure.class, name = "Failure")
})
public interface TransactionStatus {
    String getStatusType();
}