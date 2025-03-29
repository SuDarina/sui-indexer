package com.itmo.indexing_service.model.object;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.itmo.indexing_service.model.object.move.MoveObjectContent;


@JsonSubTypes({
        @JsonSubTypes.Type(value = MoveObjectContent.class, name = "Move"),
        @JsonSubTypes.Type(value = PackageObjectContent.class, name = "Package"),
        @JsonSubTypes.Type(value = CoinObjectContent.class, name = "Coin"),
        @JsonSubTypes.Type(value = ConsensusCommitPrologueContent.class, name = "ConsensusCommitPrologue"),
        @JsonSubTypes.Type(value = OtherObjectContent.class, name = "Other"),
        @JsonSubTypes.Type(value = GenesisObjectContent.class, name = "Genesis"),
        @JsonSubTypes.Type(value = WrappedObjectContent.class, name = "Wrapped")
})
public abstract class ObjectContent {
    // ObjectContent
}