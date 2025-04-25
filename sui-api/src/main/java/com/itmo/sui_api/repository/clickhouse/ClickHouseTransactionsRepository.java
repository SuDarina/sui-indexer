package com.itmo.sui_api.repository.clickhouse;

import com.itmo.model.clickhouse.MinuteTransaction;

import java.util.List;

public interface ClickHouseTransactionsRepository {
    void insertMinuteTransaction(MinuteTransaction minuteTransaction);
    void batchInsertMinuteTransactions(List<MinuteTransaction> transactions);
}
