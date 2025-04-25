package com.itmo.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "transaction_object_links")
public class TransactionObjectLink {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_digest", nullable = false)
    private IndexTransaction transaction;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "object_id", nullable = false)
//    private SuiObject object;

    @Column(name = "object_id")
    private String objectId;

    @Column(name = "object_version")
    private Long objectVersion;

    @Column(name = "object_digest", length = 128)
    private String objectDigest;

    @Enumerated(EnumType.STRING)
    @Column(name = "relation_type", nullable = false, length = 32)
    private RelationType relationType;
}
