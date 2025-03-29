package com.itmo.indexing_service.model.transcation;

import lombok.Data;

@Data
public class TransactionSuccess implements TransactionStatus {
    @Override
    public String getStatusType() {
        return "Success";
    }
}