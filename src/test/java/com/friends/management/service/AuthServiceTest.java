package com.friends.management.service;

import com.friends.management.dto.response.JwtResponse;
import com.friends.management.dto.request.LoginRequest;
import com.friends.management.entity.RoleEnum;
import com.friends.management.dto.request.SignUpRequest;
import com.friends.management.entity.Role;
import com.friends.management.entity.User;
import com.friends.management.exception.ApplicationException;
import com.friends.management.repository.RoleRepository;
import com.friends.management.repository.UserRepository;
import com.friends.management.security.jwt.JwtUtils;
import com.friends.management.security.service.UserDetailsImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthServiceTest {
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private AuthService authService;

    @MockBean
    private PasswordEncoder encoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    public void testRegister_ValidUser() {
        SignUpRequest signUpRequest = new SignUpRequest();

        Set<String> roles = new HashSet<>();
        roles.add("admin");
        roles.add("user");
        roles.add("mod");

        signUpRequest.setRole(roles);

        Mockito.when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);

        Optional<Role> roleAdmin = Optional.of(new Role());
        roleAdmin.get().setName(RoleEnum.ROLE_ADMIN.getRole());
        Mockito.when(roleRepository.findFirstByName(RoleEnum.ROLE_ADMIN.getRole())).thenReturn(roleAdmin);

        Optional<Role> roleUser = Optional.of(new Role());
        roleUser.get().setName(RoleEnum.ROLE_USER.getRole());
        Mockito.when(roleRepository.findFirstByName(RoleEnum.ROLE_USER.getRole())).thenReturn(roleUser);

        Optional<Role> roleMod = Optional.of(new Role());
        roleMod.get().setName(RoleEnum.ROLE_MODERATOR.getRole());
        Mockito.when(roleRepository.findFirstByName(RoleEnum.ROLE_MODERATOR.getRole())).thenReturn(roleMod);


        Boolean result = authService.register(signUpRequest);
        Assertions.assertTrue(result);
    }

    @Test
    public void testRegister_ExistsByUsername() {
        SignUpRequest signUpRequest = new SignUpRequest();

        Mockito.when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(true);

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            authService.register(signUpRequest);
        });

        Assertions.assertEquals("Error: Username is already taken!", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    @Test
    public void testRegister_ExistsByEmail() {
        SignUpRequest signUpRequest = new SignUpRequest();

        Mockito.when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(signUpRequest.getUsername())).thenReturn(true);

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            authService.register(signUpRequest);
        });

        Assertions.assertEquals("Error: Email is already in use!", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    @Test
    public void testRegister_EmptyRoles() {
        SignUpRequest signUpRequest = new SignUpRequest();

        Mockito.when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(signUpRequest.getUsername())).thenReturn(false);

        Optional<Role> role = Optional.of(new Role());
        role.get().setName(RoleEnum.ROLE_USER.getRole());
        Mockito.when(roleRepository.findFirstByName(RoleEnum.ROLE_USER.getRole())).thenReturn(role);

        Boolean result = authService.register(signUpRequest);
        Assertions.assertTrue(result);
    }

    @Test
    public void testLogin() {
        LoginRequest loginRequest = new LoginRequest("username", "password");
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setEmail("lexuanhuy@gmail.com");
        user.setPassword("password");

        Set<Role> roles = new HashSet<>();
        roles.add(new Role(RoleEnum.ROLE_USER.getRole()));

        user.setRoles(roles);

        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        UserDetailsImpl userDetails = new UserDetailsImpl(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), Collections.emptyList());
        String jwtToken = "jwt_token";
        List<String> rolesList = Collections.singletonList("ROLE_USER");
        JwtResponse expectedResponse = new JwtResponse(jwtToken, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), rolesList);

        Mockito.when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(encoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        Mockito.when(authenticationManager.authenticate(authentication)).thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null));
        Mockito.when(jwtUtils.generateJwtToken(userDetails)).thenReturn(jwtToken);

        JwtResponse actualResponse = authService.login(loginRequest);

        Assertions.assertEquals(expectedResponse.getToken(), actualResponse.getToken());
        Assertions.assertEquals(expectedResponse.getId(), actualResponse.getId());
        Assertions.assertEquals(expectedResponse.getUsername(), actualResponse.getUsername());
        Assertions.assertEquals(expectedResponse.getEmail(), actualResponse.getEmail());
    }
}
