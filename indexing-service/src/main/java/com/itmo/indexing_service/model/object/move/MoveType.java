package com.itmo.indexing_service.model.object.move;

import com.fasterxml.jackson.annotation.JsonSubTypes;

@JsonSubTypes({
        @JsonSubTypes.Type(value = OtherMoveType.class, name = "Other"),
        @JsonSubTypes.Type(value = VectorMoveType.class, name = "Vector"),
        @JsonSubTypes.Type(value = StructMoveType.class, name = "Struct"),
        @JsonSubTypes.Type(value = StringMoveType.class, name = "String")
})
public abstract class MoveType {
    // Общие поля или методы
}