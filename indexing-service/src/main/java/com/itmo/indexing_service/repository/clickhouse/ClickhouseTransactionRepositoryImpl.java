package com.itmo.indexing_service.repository.clickhouse;

import com.itmo.indexing_service.config.ClickHouseJdbcTemplate;
import com.itmo.model.clickhouse.TransactionAnalyticsRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClickhouseTransactionRepositoryImpl implements ClickhouseTransactionRepository {

    private final ClickHouseJdbcTemplate clickHouseJdbcTemplate;

    private static final String INSERT_SQL = """
            INSERT INTO transaction_analytics (
                date, datetime, hour, minute, transaction_digest, sender, executed_epoch,
                status, gas_budget, gas_price, gas_used, computation_cost, storage_cost, storage_rebate,
                created_objects, mutated_objects, deleted_objects, wrapped_objects,
                unwrapped_objects, unwrapped_then_deleted_objects, shared_objects,
                address_owned, object_owned, shared_owned, immutable_owned, total_objects,
                checkpoint_seq, dependencies
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

    @Override
    public void saveTransactionAnalytics(TransactionAnalyticsRecord transactionAnalytics) {
        clickHouseJdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_SQL);
            setValues(ps, transactionAnalytics);
            return ps;
        });
    }

    @Override
    public void batchSaveTransactionAnalytics(List<TransactionAnalyticsRecord> batchList) throws SQLException {
        clickHouseJdbcTemplate.batchUpdate(
                INSERT_SQL,
                batchList,
                500,
                (PreparedStatement ps, TransactionAnalyticsRecord record) -> setValues(ps, record)
        );
    }

    private void setValues(PreparedStatement ps, TransactionAnalyticsRecord record) throws SQLException {
        ps.setDate(1, Date.valueOf(record.getDate()));
        ps.setTimestamp(2, Timestamp.valueOf(record.getDatetime()));
        ps.setInt(3, record.getHour());
        ps.setInt(4, record.getMinute());
        ps.setString(5, record.getTransactionDigest());
        ps.setString(6, record.getSender());
        ps.setLong(7, record.getExecutedEpoch());
        ps.setString(8, record.getStatus());

        ps.setLong(9, record.getGasBudget());
        ps.setLong(10, record.getGasPrice());
        ps.setLong(11, record.getGasUsed());
        ps.setLong(12, record.getComputationCost());
        ps.setLong(13, record.getStorageCost());
        ps.setLong(14, record.getStorageRebate());

        ps.setInt(15, record.getCreatedObjects());
        ps.setInt(16, record.getMutatedObjects());
        ps.setInt(17, record.getDeletedObjects());
        ps.setInt(18, record.getWrappedObjects());
        ps.setInt(19, record.getUnwrappedObjects());
        ps.setInt(20, record.getUnwrappedThenDeletedObjects());
        ps.setInt(21, record.getSharedObjects());

        ps.setInt(22, record.getAddressOwned());
        ps.setInt(23, record.getObjectOwned());
        ps.setInt(24, record.getSharedOwned());
        ps.setInt(25, record.getImmutableOwned());
        ps.setInt(26, record.getTotalObjects());
        ps.setLong(27, record.getCheckpointSeq());

        List<String> deps = Optional.ofNullable(record.getDependencies()).orElse(List.of());
        String depsAsArrayString = deps.stream()
                .map(dep -> "'" + dep.replace("'", "\\'") + "'")
                .collect(Collectors.joining(", ", "[", "]"));

        ps.setString(28, depsAsArrayString);
    }
}
