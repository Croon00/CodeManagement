package com.kstec.codemangement.service;

import com.kstec.codemangement.config.CustomUserDetails;

public interface AuthService {
    String refreshAccessToken();
    CustomUserDetails getAuthenticatedUser();
}
