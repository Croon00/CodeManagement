package com.kstec.codemangement.model.repository;

import com.kstec.codemangement.model.document.CodeSearchLogDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeSearchLogRepository extends ElasticsearchRepository<CodeSearchLogDocument, String> {
}
