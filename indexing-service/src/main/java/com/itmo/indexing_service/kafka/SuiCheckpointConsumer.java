package com.itmo.indexing_service.kafka;

import com.itmo.indexing_service.model.checkpoint.CheckpointData;
import com.itmo.indexing_service.model.transcation.util.ClickhouseEntitiesParser;
import com.itmo.indexing_service.model.transcation.util.PostgresEntitiesParser;
import com.itmo.indexing_service.repository.clickhouse.ClickhouseTransactionRepository;
import com.itmo.indexing_service.repository.postgres.PostgresTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class SuiCheckpointConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final PostgresTransactionRepository postgresTransactionRepository;
    private final ClickhouseTransactionRepository clickhouseTransactionRepository;

    @KafkaListener(topics = "sui-checkpoints", groupId = "sui-indexer")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            CheckpointData checkpoint = objectMapper.readValue(record.value(), new TypeReference<>() {});
            log.debug("Received checkpoint: {}", checkpoint.getCheckpointSummary().getData().getSequenceNumber());
            postgresTransactionRepository.saveAll(PostgresEntitiesParser.parseToListTransactions(checkpoint));
            clickhouseTransactionRepository.batchSaveTransactionAnalytics(
                    ClickhouseEntitiesParser.mapToTransactionAnalyticsRecords(checkpoint)
            );
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
