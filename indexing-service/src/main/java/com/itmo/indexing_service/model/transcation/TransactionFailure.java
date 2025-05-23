package com.itmo.indexing_service.model.transcation;

import lombok.Data;

@Data
public class TransactionFailure implements TransactionStatus {
    private Object error;
    private Integer command;

    @Override
    public String getStatusType() {
        return "Failure";
    }
}