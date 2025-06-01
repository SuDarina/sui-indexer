package com.itmo.indexing_service.kafka;

import com.itmo.indexing_service.model.checkpoint.CheckpointData;
import com.itmo.indexing_service.model.transcation.util.ClickhouseEntitiesParser;
import com.itmo.indexing_service.model.transcation.util.PostgresEntitiesParser;
import com.itmo.indexing_service.repository.clickhouse.ClickhouseTransactionRepository;
import com.itmo.indexing_service.repository.postgres.PostgresTransactionRepository;
import com.itmo.indexing_service.service.IndexingService;
import com.itmo.model.clickhouse.BatchClickhouseInstance;
import com.itmo.model.clickhouse.TransactionAnalyticsRecord;
import com.itmo.model.postgres.BatchPostgresInstance;
import com.itmo.model.postgres.IndexTransaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SuiCheckpointConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

//    private final PostgresTransactionRepository postgresTransactionRepository;
//    private final ClickhouseTransactionRepository clickhouseTransactionRepository;
    private final IndexingService indexingService;

    @KafkaListener(topics = "sui-checkpoints", groupId = "sui-indexer")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            CheckpointData checkpoint = objectMapper.readValue(record.value(), new TypeReference<>() {});
            BigInteger sequenceNumber = checkpoint.getCheckpointSummary().getData().getSequenceNumber();
            log.debug("Received checkpoint: {}", sequenceNumber);
            indexingService.batchInsertWithMetrics(formBatchPostgresInstance(PostgresEntitiesParser.parseToListTransactions(checkpoint)),
                    formBatchClickhouseInstance(ClickhouseEntitiesParser.mapToTransactionAnalyticsRecords(checkpoint)));
//            postgresTransactionRepository.saveAll(PostgresEntitiesParser.parseToListTransactions(checkpoint));
//            clickhouseTransactionRepository.batchSaveTransactionAnalytics(
//                    ClickhouseEntitiesParser.mapToTransactionAnalyticsRecords(checkpoint)
//            );
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private BatchPostgresInstance formBatchPostgresInstance(List<IndexTransaction> indexTransactions) {
        return BatchPostgresInstance.builder()
                .indexTransactions(indexTransactions)
                .build();
    }

    private BatchClickhouseInstance formBatchClickhouseInstance(List<TransactionAnalyticsRecord> transactionAnalyticsRecords) {
        return BatchClickhouseInstance.builder()
                .transactionAnalyticsRecords(transactionAnalyticsRecords)
                .build();
    }
}
