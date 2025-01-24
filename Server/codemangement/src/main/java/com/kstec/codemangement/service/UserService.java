package com.kstec.codemangement.service;

import com.kstec.codemangement.model.dto.requestdto.UserRequestDto;
import com.kstec.codemangement.model.dto.responsedto.UserResponseDto;

public interface UserService {

    UserResponseDto registerUser(UserRequestDto userRequestDto);
}
