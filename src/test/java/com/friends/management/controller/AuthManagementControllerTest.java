package com.friends.management.controller;

import com.friends.management.response.JwtResponse;
import com.friends.management.dto.LoginRequest;
import com.friends.management.dto.SignUpRequest;
import com.friends.management.security.jwt.AuthenticationEntryPointHandler;
import com.friends.management.security.jwt.JwtUtils;
import com.friends.management.security.service.UserDetailsServiceImpl;
import com.friends.management.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(AuthManagementController.class)
public class AuthManagementControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private AuthenticationEntryPointHandler authenticationEntryPointHandler;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    public void testLogin() throws Exception {
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setId(1L);
        jwtResponse.setUsername("admin123");
        jwtResponse.setEmail("lexuanhuy@gmail.com");
        jwtResponse.setToken("abc");

        List<String> roles = new ArrayList<>();
        roles.add("ROLE_ADMIN");
        jwtResponse.setRoles(roles);

        when(authService.login(any(LoginRequest.class))).thenReturn(jwtResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON).content("{ \"username\": \"admin123\", \"password\": \"huy12345\" }"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(jwtResponse.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(jwtResponse.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(jwtResponse.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(jwtResponse.getToken()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles").value(jwtResponse.getRoles()));
    }

    @Test
    public void tesRegister() throws Exception {
        when(authService.register(any(SignUpRequest.class))).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON).content("{ \"username\": \"truong\", \"password\": \"huy12345\", \"email\": \"truong@gmail.com\", \"role\": [ \"user\" ] }"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
    }
}
