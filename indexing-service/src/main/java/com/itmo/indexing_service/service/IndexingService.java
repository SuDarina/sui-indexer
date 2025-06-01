package com.itmo.indexing_service.service;

import com.itmo.indexing_service.repository.clickhouse.ClickhouseTransactionRepository;
import com.itmo.indexing_service.repository.postgres.PostgresTransactionRepository;
import com.itmo.model.clickhouse.BatchClickhouseInstance;
import com.itmo.model.postgres.BatchPostgresInstance;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndexingService {

    private final PostgresTransactionRepository postgresTransactionRepository;
    private final ClickhouseTransactionRepository clickhouseTransactionRepository;
    private final MeterRegistry meterRegistry;

    public void batchInsert(BatchPostgresInstance postgresInstance,
                            BatchClickhouseInstance clickhouseInstance) throws SQLException {
        postgresTransactionRepository.saveAll(postgresInstance.getIndexTransactions());
        clickhouseTransactionRepository.batchSaveTransactionAnalytics(clickhouseInstance.getTransactionAnalyticsRecords());
    }

    public void batchInsertWithMetrics(BatchPostgresInstance postgresInstance,
                                       BatchClickhouseInstance clickhouseInstance) throws SQLException {
        Timer.Sample sample = Timer.start(meterRegistry);
        String status = "success";
        try {
            batchInsert(postgresInstance, clickhouseInstance);
        } catch (SQLException e) {
            status = "fail";
            throw e;
        } finally {
            sample.stop(meterRegistry.timer(
                    "indexing_checkpoint_latency_ms",
                    "status", status
            ));
        }
    }
}
