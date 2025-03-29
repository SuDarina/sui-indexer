package com.itmo.indexing_service.model.object.move;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class OtherMoveType extends MoveType {
    private String address;
    private String module;
    private String name;

    @JsonProperty("type_args")
    private Object typeArgs;
}
