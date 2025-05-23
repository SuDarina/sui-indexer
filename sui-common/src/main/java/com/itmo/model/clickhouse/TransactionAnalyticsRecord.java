package com.itmo.model.clickhouse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionAnalyticsRecord {

    // Время
    private LocalDate date;
    private LocalDateTime datetime;
    private int hour;
    private int minute;

    // Идентификаторы
    private String transactionDigest;
    private String sender;

    // Эпоха исполнения
    private long executedEpoch;

    // Статус
    private String status;

    // Gas usage
    private long gasBudget;
    private long gasPrice;
    private long gasUsed;
    private long computationCost;
    private long storageCost;
    private long storageRebate;

    // Объекты
    private int createdObjects;
    private int mutatedObjects;
    private int deletedObjects;
    private int wrappedObjects;
    private int unwrappedObjects;
    private int unwrappedThenDeletedObjects;
    private int sharedObjects;

    // Владение
    private int addressOwned;
    private int objectOwned;
    private int sharedOwned;
    private int immutableOwned;

    // Сводная метрика
    private int totalObjects;

    // Чекпоинт
    private long checkpointSeq;

    // Зависимости (опционально)
    private List<String> dependencies;
}
