package com.itmo.sui_api.controller;

import com.itmo.model.clickhouse.MinuteTransaction;
import com.itmo.sui_api.repository.clickhouse.ClickHouseTransactionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sui-api/v1/transactions")
public class TransactionsController {

    private final ClickHouseTransactionsRepository clickHouseTransactionsRepository;

    @PostMapping
    public ResponseEntity<String> insertMinuteTransaction(@RequestBody MinuteTransaction transaction) {
        clickHouseTransactionsRepository.insertMinuteTransaction(transaction);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
