package com.kstec.codemangement.model.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "code_search_log_index")
//@Mapping(mappingPath = "/elasticsearch/log_mappings.json")
//@Setting(settingPath = "/elasticsearch/settings.json")
public class CodeSearchLogDocument {

    @Id
    private String id; // ✅ Elasticsearch가 자동 생성

    @Field(type = FieldType.Long)
    private Long codeId;

    @Field(type = FieldType.Text, analyzer = "ngram_analyzer", searchAnalyzer = "standard")
    private String codeName;

    @Field(type = FieldType.Text, index = false)
    private String userId;


    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime searchedAt;


    @Builder
    public CodeSearchLogDocument(String userId, Long codeId, String codeName, LocalDateTime searchedAt) {
        this.userId = userId;
        this.codeId = codeId;
        this.codeName = codeName;
        this.searchedAt = searchedAt;
    }
}
