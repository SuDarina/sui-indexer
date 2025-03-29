package com.itmo.indexing_service.model.object.move;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StringMoveType extends MoveType {
    private String value;
}