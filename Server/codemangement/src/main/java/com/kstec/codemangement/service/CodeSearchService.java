package com.kstec.codemangement.service;

import com.kstec.codemangement.model.dto.responsedto.CodeResponseDto;

import java.util.List;

public interface CodeSearchService {

    public List<CodeResponseDto> searchCodes(String searchType, String query, String startDate, String endDate, Boolean activated, Long parentCodeId);

}
