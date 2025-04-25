package com.itmo.indexing_service.model.transcation.util;

import com.itmo.indexing_service.model.checkpoint.CheckpointData;
import com.itmo.indexing_service.model.transcation.Transaction;
import com.itmo.indexing_service.model.transcation.TransactionEffectsV1;
import com.itmo.indexing_service.model.object.objectRef.ObjectWithOwner;

import com.itmo.model.clickhouse.TransactionAnalyticsRecord;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class ClickhouseEntitiesParser {

    public static TransactionAnalyticsRecord mapToAnalyticsRecord(Transaction transactionDto, BigInteger timestampMs, long checkpointSeq) {
        TransactionEffectsV1 effects = transactionDto.getEffects().getV1();

        Instant instant = Instant.ofEpochMilli(timestampMs.longValue());
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);

        List<ObjectWithOwner> created = flatten(effects.getCreated());
        List<ObjectWithOwner> mutated = flatten(effects.getMutated());
        List<ObjectWithOwner> deleted = flatten(effects.getDeleted());
        List<ObjectWithOwner> wrapped = flatten(effects.getWrapped());
        List<ObjectWithOwner> unwrapped = flatten(effects.getUnwrapped());
        List<ObjectWithOwner> unwrappedThenDeleted = flatten(effects.getUnwrappedThenDeleted());

        // Count ownership types
        long addressOwned = Stream.of(created, mutated, unwrapped)
                .flatMap(List::stream)
                .filter(o -> o.getOwnership().getType().name().equals("AddressOwner"))
                .count();

        long objectOwned = Stream.of(created, mutated, unwrapped)
                .flatMap(List::stream)
                .filter(o -> o.getOwnership().getType().name().equals("ObjectOwner"))
                .count();

        long sharedOwned = Stream.of(created, mutated, unwrapped)
                .flatMap(List::stream)
                .filter(o -> o.getOwnership().getType().name().equals("Shared"))
                .count();

        long immutableOwned = Stream.of(created, mutated, unwrapped)
                .flatMap(List::stream)
                .filter(o -> o.getOwnership().getType().name().equals("Immutable"))
                .count();

        TransactionAnalyticsRecord transactionAnalyticsRecord = TransactionAnalyticsRecord.builder()
                .date(dateTime.toLocalDate())
                .datetime(dateTime)
                .hour(dateTime.getHour())
                .minute(dateTime.getMinute())

                .transactionDigest(effects.getTransactionDigest())
                .sender(transactionDto.getTransaction()
                        .getData().getFirst()
                        .getIntentMessage()
                        .getValue().getV1().getSender())

                .executedEpoch(effects.getExecutedEpoch().longValue())
                .status(effects.getStatus().getStatusType())

                .gasBudget(getSafe(transactionDto.getTransaction()
                        .getData().getFirst()
                        .getIntentMessage()
                        .getValue().getV1().getGasData().getBudget()))

                .gasPrice(getSafe(transactionDto.getTransaction()
                        .getData().getFirst()
                        .getIntentMessage()
                        .getValue().getV1().getGasData().getPrice()))

                .createdObjects(created.size())
                .mutatedObjects(mutated.size())
                .deletedObjects(deleted.size())
                .wrappedObjects(wrapped.size())
                .unwrappedObjects(unwrapped.size())
                .unwrappedThenDeletedObjects(unwrappedThenDeleted.size())
                .sharedObjects(effects.getSharedObjects() != null ? effects.getSharedObjects().size() : 0)

                .addressOwned((int) addressOwned)
                .objectOwned((int) objectOwned)
                .sharedOwned((int) sharedOwned)
                .immutableOwned((int) immutableOwned)

                .totalObjects(
                        created.size() + mutated.size() + deleted.size() + wrapped.size()
                                + unwrapped.size() + unwrappedThenDeleted.size()
                )

                .checkpointSeq(checkpointSeq)
                .dependencies(effects.getDependencies() != null ? effects.getDependencies() : Collections.emptyList())

                .build();
        if (effects.getGasUsed() != null) {
            long computationCost = getSafe(effects.getGasUsed().getComputationCost());
            long storageCost = getSafe(effects.getGasUsed().getStorageCost());
            long storageRebate = getSafe(effects.getGasUsed().getStorageRebate());
            long gasUsed = computationCost + storageCost + storageRebate;

            transactionAnalyticsRecord.setComputationCost(computationCost);
            transactionAnalyticsRecord.setStorageCost(storageCost);
            transactionAnalyticsRecord.setStorageRebate(storageRebate);
            transactionAnalyticsRecord.setGasUsed(gasUsed);
        }
        return transactionAnalyticsRecord;
    }

    private static long getSafe(Long value) {
        return value != null ? value : 0L;
    }

    private static List<ObjectWithOwner> flatten(List<?> raw) {
        if (raw == null || raw.isEmpty()) return Collections.emptyList();
        if (raw.get(0) instanceof ObjectWithOwner) {
            return (List<ObjectWithOwner>) raw;
        } else if (raw.get(0) instanceof List) {
            return ((List<List<ObjectWithOwner>>) raw).stream().flatMap(List::stream).toList();
        }
        return Collections.emptyList();
    }

    public static List<TransactionAnalyticsRecord> mapToTransactionAnalyticsRecords(CheckpointData checkpoint) {
        List<Transaction> transactions = checkpoint.getTransactions();
        BigInteger timestampMs = checkpoint.getCheckpointSummary().getData().getTimestampMs();
        long checkpointSeq = checkpoint.getCheckpointSummary().getData().getSequenceNumber().longValue();

        return transactions.stream().map(transaction -> mapToAnalyticsRecord(transaction, timestampMs, checkpointSeq)).toList();
    }
}
