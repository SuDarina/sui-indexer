package com.itmo.model.postgres;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class IndexTransaction {

    @Id
    @Column(name = "digest", nullable = false, length = 128)
    private String digest;

    @Column(name = "sender", nullable = false, length = 128)
    private String sender;

    @Column(name = "timestamp_ms", nullable = false)
    private BigInteger timestamp;

    @Column(name = "gas_price")
    private Long gasPrice;

    @Column(name = "gas_budget")
    private Long gasBudget;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionObjectLink> objectLinks;

}
