package com.itmo.sui_api.repository.postgres;

import com.itmo.model.postgres.IndexTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostgresTransactionRepository extends JpaRepository<IndexTransaction, String> {
}
