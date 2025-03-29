package com.itmo.indexing_service.model.object.move;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class StructMoveType extends MoveType {
    private String address;
    private String module;
    private String name;
    private List<MoveType> type_args;
}