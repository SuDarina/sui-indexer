package com.itmo.indexing_service.kafka;

import com.itmo.indexing_service.model.checkpoint.CheckpointData;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

@Service
@Slf4j
public class SuiCheckpointConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "sui-checkpoints", groupId = "sui-indexer")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            // Десериализация JSON
            long startTime = System.currentTimeMillis();
            CheckpointData checkpoint = objectMapper.readValue(record.value(), new TypeReference<>() {});
            log.debug("readvalue execution: " + (System.currentTimeMillis() - startTime));
            // Обработка чекпоинта (например, сохранение в базу данных)
            log.debug("Received checkpoint: {}", checkpoint.getCheckpointSummary().getData().getSequenceNumber());
            // Ваша логика обработки...

        } catch (Exception e) {
            System.out.println("record: " + record.value());
            e.printStackTrace();
        }
    }
}
