package com.itmo.sui_api.repository.clickhouse;

import com.itmo.model.clickhouse.MinuteTransaction;

import java.util.List;

public interface TransactionsRepository {
    void insertTransaction(MinuteTransaction minuteTransaction);
    void batchInsertTransactions(List<MinuteTransaction> transactions);
}
