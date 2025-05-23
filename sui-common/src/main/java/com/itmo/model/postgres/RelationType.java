package com.itmo.model.postgres;

public enum RelationType {
    INPUT,
    OUTPUT,
    MUTATED,
    CREATED,
    UNWRAPPED,
    WRAPPED,
    DELETED,
    GAS,
    UNWRAPPED_THEN_DELETED
}
