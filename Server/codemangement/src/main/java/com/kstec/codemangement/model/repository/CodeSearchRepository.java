package com.kstec.codemangement.model.repository;

import com.kstec.codemangement.model.document.CodeDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeSearchRepository extends ElasticsearchRepository<CodeDocument, Long> {

    List<CodeDocument> searchByCodeName(String codeName);

    List<CodeDocument> searchByCodeValue(String codeValue);

}
