package com.itmo.model.clickhouse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MinuteTransaction {
    private LocalDate date;
    private int hour;
    private int minute;
    private long totalTx;
    private long successfulTx;
    private long failedTx;
    private long gasUsedTotal;
    private double gasUsedAvg;
    private long uniqueSenders;
    private long uniqueReceivers;
    private long contractCallsCount;
    private long nftTransfersCount;
    private long defiSwapsCount;
    private double volumeTotal;
}