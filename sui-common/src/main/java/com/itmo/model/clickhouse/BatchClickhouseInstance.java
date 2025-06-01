package com.itmo.model.clickhouse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class BatchClickhouseInstance {
    private List<TransactionAnalyticsRecord> transactionAnalyticsRecords;
}
