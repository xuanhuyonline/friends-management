package com.friends.management.service;

import com.friends.management.response.JwtResponse;
import com.friends.management.dto.LoginRequest;
import com.friends.management.dto.SignUpRequest;

public interface AuthService {
    JwtResponse login(LoginRequest loginRequest);

    Boolean register(SignUpRequest signUpRequest);
}
