package com.itmo.indexing_service.model.object.objectRef;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itmo.indexing_service.model.object.owner.Ownership;
import lombok.Data;

//@JsonDeserialize(using = ObjectWithOwnerDeserializer.class)
@Data
public class ObjectWithOwner {

    private final ObjectRef objectRef;
    private final Ownership ownership;

    @JsonCreator
    public ObjectWithOwner(
            @JsonProperty("0") ObjectRef objectRef,
            @JsonProperty("1") Ownership ownership) {
        this.objectRef = objectRef;
        this.ownership = ownership;
    }
}
