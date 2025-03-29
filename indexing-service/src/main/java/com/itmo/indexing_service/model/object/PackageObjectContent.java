package com.itmo.indexing_service.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class PackageObjectContent extends ObjectContent {
    @JsonProperty("id")
    private String id;

    @JsonProperty("version")
    private Long version;

    @JsonProperty("module_map")
    private Map<String, List<Integer>> moduleMap;

    @JsonProperty("type_origin_table")
    private List<TypeOrigin> typeOriginTable;

    @JsonProperty("linkage_table")
    private Map<String, LinkageInfo> linkageTable;


    @Data
    public static class TypeOrigin {
        @JsonProperty("module_name")
        private String moduleName;

        @JsonProperty("datatype_name")
        private String datatypeName;

        @JsonProperty("package")
        private String packageId;
    }

    @Data
    public static class LinkageInfo {
        @JsonProperty("upgraded_id")
        private String upgradedId;

        @JsonProperty("upgraded_version")
        private Long upgradedVersion;
    }
}