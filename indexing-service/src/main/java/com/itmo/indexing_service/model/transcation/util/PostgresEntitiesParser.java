package com.itmo.indexing_service.model.transcation.util;

import com.itmo.indexing_service.model.checkpoint.CheckpointData;
import com.itmo.indexing_service.model.object.objectRef.ObjectRef;
import com.itmo.indexing_service.model.object.objectRef.ObjectWithOwner;
import com.itmo.indexing_service.model.transcation.Transaction;
import com.itmo.indexing_service.model.transcation.TransactionEffectsV1;
import com.itmo.model.postgres.IndexTransaction;
import com.itmo.model.postgres.RelationType;
import com.itmo.model.postgres.SuiObject;
import com.itmo.model.postgres.TransactionObjectLink;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public final class PostgresEntitiesParser {

    public static List<IndexTransaction> parseToListTransactions(CheckpointData checkpointData) {

        return checkpointData.getTransactions()
                .stream()
                .map(transaction ->
                        parseToPostgresTransaction(
                                transaction,
                                checkpointData.getCheckpointSummary().getData().getTimestampMs()
                        )
                ).toList();
    }

    public static IndexTransaction parseToPostgresTransaction (Transaction transactionDto, BigInteger timestamp) {
        IndexTransaction indexTransaction = IndexTransaction.builder()
                .digest(transactionDto.getEffects().getV1().getTransactionDigest())
                .sender(transactionDto.getTransaction()
                        .getData().getFirst()
                        .getIntentMessage()
                        .getValue().getV1().getSender())
                .timestamp(timestamp)
                .gasPrice(transactionDto.getTransaction()
                        .getData().getFirst()
                        .getIntentMessage()
                        .getValue().getV1().getGasData().getPrice())
                .gasBudget(transactionDto.getTransaction()
                        .getData().getFirst()
                        .getIntentMessage()
                        .getValue().getV1().getGasData().getBudget())
                .build();

        List<TransactionObjectLink> listTransactionObjectLink = mapFromEffects(transactionDto.getEffects().getV1(), indexTransaction);
        indexTransaction.setObjectLinks(listTransactionObjectLink);
        return indexTransaction;
    }

    private static List<TransactionObjectLink> mapFromEffects(TransactionEffectsV1 effects, IndexTransaction indexTransaction) {
        List<TransactionObjectLink> links = new ArrayList<>();

        addLinks(links, flatten(effects.getCreated()), RelationType.CREATED, indexTransaction);
        addLinks(links, flatten(effects.getMutated()), RelationType.MUTATED, indexTransaction);
        addLinks(links, flatten(effects.getUnwrapped()), RelationType.UNWRAPPED, indexTransaction);
        addLinks(links, flatten(effects.getDeleted()), RelationType.DELETED, indexTransaction);
        addLinks(links, flatten(effects.getUnwrappedThenDeleted()), RelationType.UNWRAPPED_THEN_DELETED, indexTransaction);
        addLinks(links, flatten(effects.getWrapped()), RelationType.WRAPPED, indexTransaction);
        addLinks(links, flatten(effects.getGasObject()), RelationType.GAS, indexTransaction);

        return links;
    }

    private static void addLinks(List<TransactionObjectLink> result, List<ObjectWithOwner> objects, RelationType type, IndexTransaction transaction) {
        for (ObjectWithOwner obj : objects) {
            ObjectRef ref = obj.getObjectRef();

            TransactionObjectLink link = new TransactionObjectLink();
            link.setTransaction(transaction);
            link.setObjectId(ref.getObjectId());
//            link.setObject(SuiObject.builder()
//                    .objectId(ref.getObjectId())
//                    .latestVersion(ref.getVersion())
//                    .latestDigest(ref.getDigest())
//                    .build());
            link.setRelationType(type);
            link.setObjectVersion(ref.getVersion());
            link.setObjectDigest(ref.getDigest());

            result.add(link);
        }
    }

    private static List<ObjectWithOwner> flatten(List<?> nestedList) {
        List<ObjectWithOwner> flat = new ArrayList<>();
        flattenRecursive(nestedList, flat);
        return flat;
    }

    @SuppressWarnings("unchecked")
    private static void flattenRecursive(List<?> list, List<ObjectWithOwner> flat) {
        for (Object item : list) {
            if (item instanceof ObjectWithOwner) {
                flat.add((ObjectWithOwner) item);
            } else if (item instanceof List<?>) {
                flattenRecursive((List<?>) item, flat);
            }
        }
    }
}
