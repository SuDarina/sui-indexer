package com.itmo.model.postgres;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "objects")
public class SuiObject {

    @Id
    @Column(name = "object_id", nullable = false, length = 128)
    private String objectId;

    @Column(name = "latest_version")
    private Long latestVersion;

    @Column(name = "latest_digest", length = 128)
    private String latestDigest;
}
