package com.itmo.indexing_service.repository.clickhouse;

import com.itmo.model.clickhouse.TransactionAnalyticsRecord;

import java.sql.SQLException;
import java.util.List;

public interface ClickhouseTransactionRepository {
    void saveTransactionAnalytics(TransactionAnalyticsRecord transactionAnalytics);
    void batchSaveTransactionAnalytics(List<TransactionAnalyticsRecord> transactionAnalyticsList) throws SQLException;
}
