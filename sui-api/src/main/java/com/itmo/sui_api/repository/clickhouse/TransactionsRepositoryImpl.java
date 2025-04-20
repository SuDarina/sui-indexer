package com.itmo.sui_api.repository.clickhouse;

import com.itmo.model.clickhouse.MinuteTransaction;
import com.itmo.sui_api.config.ClickHouseJdbcTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionsRepositoryImpl implements TransactionsRepository {

    private final ClickHouseJdbcTemplate clickHouseJdbcTemplate;

    @Override
    public void insertTransaction(MinuteTransaction transaction) {
        String sql = """
            
                INSERT INTO suiaggr.minute_transactions (
                date, hour, minute, total_tx, successful_tx, failed_tx,
                gas_used_total, gas_used_avg, unique_senders, unique_receivers,
                contract_calls_count, nft_transfers_count, defi_swaps_count, volume_total
                            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        clickHouseJdbcTemplate.update(sql,
                transaction.getDate(),
                transaction.getHour(),
                transaction.getMinute(),
                transaction.getTotalTx(),
                transaction.getSuccessfulTx(),
                transaction.getFailedTx(),
                transaction.getGasUsedTotal(),
                transaction.getGasUsedAvg(),
                transaction.getUniqueSenders(),
                transaction.getUniqueReceivers(),
                transaction.getContractCallsCount(),
                transaction.getNftTransfersCount(),
                transaction.getDefiSwapsCount(),
                transaction.getVolumeTotal()
        );
    }

    @Override
    public void batchInsertTransactions(List<MinuteTransaction> transactions) {
        String sql =
                """
    
                                            INSERT INTO 
                                    minute_transa
                                    date, hour, minute, t
                                    successful_tx, fa
                                    gas_use
                                    gas_used_avg, unique_senders, unique_re
                                    contract_call
                                    nft_transfers_count, defi_swaps_c
                                              ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 
                                """;

        clickHouseJdbcTemplate.batchUpdate(sql,
                transactions.stream()
                        .map(t -> new Object[]{
                                t.getDate(),
                                t.getHour(),
                                t.getMinute(),
                                t.getTotalTx(),
                                t.getSuccessfulTx(),
                                t.getFailedTx(),
                                t.getGasUsedTotal(),
                                t.getGasUsedAvg(),
                                t.getUniqueSenders(),
                                t.getUniqueReceivers(),
                                t.getContractCallsCount(),
                                t.getNftTransfersCount(),
                                t.getDefiSwapsCount(),
                                t.getVolumeTotal()
                        })
                        .collect(Collectors.toList())
        );
    }
}
