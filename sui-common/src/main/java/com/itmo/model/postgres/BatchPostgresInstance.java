package com.itmo.model.postgres;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class BatchPostgresInstance {
    private List<IndexTransaction> indexTransactions;
}
