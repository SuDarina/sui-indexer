package com.itmo.indexing_service.model.object.move;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VectorMoveType extends MoveType {
    private MoveType elementType;
}