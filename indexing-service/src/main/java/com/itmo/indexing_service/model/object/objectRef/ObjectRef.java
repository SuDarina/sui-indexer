package com.itmo.indexing_service.model.object.objectRef;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ObjectRef {

    private final String objectId;
    private final long version;
    private final String digest;

    @JsonCreator
    public ObjectRef(
            @JsonProperty("0") String objectId,
            @JsonProperty("1") long version,
            @JsonProperty("2") String digest) {
        this.objectId = objectId;
        this.version = version;
        this.digest = digest;
    }
}
