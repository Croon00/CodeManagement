package com.kstec.codemangement.model.document;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Document(indexName = "code_index")
@Mapping(mappingPath = "/elasticsearch/mappings.json")
@Setting(settingPath = "/elasticsearch/settings.json")
public class CodeDocument {

    @Id
    private Long codeId;

    @Field(type = FieldType.Text, analyzer = "ngram_analyzer", searchAnalyzer = "standard")
    private String codeName;

    @Field(type = FieldType.Text, analyzer = "ngram_analyzer", searchAnalyzer = "standard")
    private String codeValue;

    @Field(type = FieldType.Boolean)
    private Boolean activated;

    @Field(type = FieldType.Date, format = DateFormat.date_optional_time)
    private LocalDateTime createdAt;

    @Field(type = FieldType.Date, format = DateFormat.date_optional_time)
    private LocalDateTime updatedAt;

    @Field(type = FieldType.Text)
    private String codeMean;

    @Field(type = FieldType.Long)
    private Long parentCodeId;

    @Builder
    public CodeDocument(Long codeId, String codeName, String codeValue, Boolean activated, LocalDateTime createdAt, LocalDateTime updatedAt, String codeMean, Long parentCodeId) {
        this.codeId = codeId;
        this.codeName = codeName;
        this.codeValue = codeValue;
        this.activated = activated;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.codeMean = codeMean;
        this.parentCodeId = parentCodeId;
    }
}
