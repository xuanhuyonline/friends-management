package com.friends.management.controller;

import com.friends.management.dto.response.JwtResponse;
import com.friends.management.dto.response.SuccessResponse;
import com.friends.management.dto.request.LoginRequest;
import com.friends.management.dto.request.SignUpRequest;
import com.friends.management.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthManagementController {
    private final AuthService authService;

    @PostMapping("/signin")
    public JwtResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/signup")
    public SuccessResponse register(@Valid @RequestBody SignUpRequest signUpRequest) {
        Boolean success = authService.register(signUpRequest);
        return new SuccessResponse(success);
    }

}
