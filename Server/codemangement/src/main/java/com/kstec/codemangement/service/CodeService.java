package com.kstec.codemangement.service;

import com.kstec.codemangement.model.dto.requestdto.CodeRequestDto;
import com.kstec.codemangement.model.dto.requestdto.CodeSearchLogRequestDto;
import com.kstec.codemangement.model.dto.responsedto.CodeResponseDto;
import com.kstec.codemangement.model.dto.responsedto.CodeSearchLogResponseDto;

import java.util.List;

public interface CodeService {

    CodeResponseDto getCodeById(Long codeId);

    List<CodeResponseDto> getAllCodes();

//    List<CodeResponseDto> searchCodes(String searchType, String query, String startDate, String endDate, Boolean activated, Long parentCodeI);

    CodeResponseDto createCode(CodeRequestDto codeRequestDto);

    CodeResponseDto updateCode(Long codeId, CodeRequestDto codeRequestDto);

}
