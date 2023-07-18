package com.friends.management.service;

import com.friends.management.dto.response.JwtResponse;
import com.friends.management.dto.request.LoginRequest;
import com.friends.management.dto.request.SignUpRequest;

public interface AuthService {
    JwtResponse login(LoginRequest loginRequest);

    Boolean register(SignUpRequest signUpRequest);
}
