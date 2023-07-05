package com.friends.management.service.impl;

import com.friends.management.aspect.AuditLog;
import com.friends.management.aspect.LogExecutionTime;
import com.friends.management.common.JwtResponse;
import com.friends.management.dto.LoginRequest;
import com.friends.management.dto.RoleEnum;
import com.friends.management.dto.SignUpRequest;
import com.friends.management.entity.Role;
import com.friends.management.entity.User;
import com.friends.management.exception.ApplicationException;
import com.friends.management.repository.RoleRepository;
import com.friends.management.repository.UserRepository;
import com.friends.management.security.jwt.JwtUtils;
import com.friends.management.security.service.UserDetailsImpl;
import com.friends.management.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @LogExecutionTime
    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
    }

    @AuditLog(action = "create_account")
    @Override
    public Boolean register(SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new ApplicationException("Error: Username is already taken!", HttpStatus.BAD_REQUEST.value());
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ApplicationException("Error: Email is already in use!", HttpStatus.BAD_REQUEST.value());
        }

        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        setUserRoles(signUpRequest, user);

        userRepository.save(user);

        return true;
    }

    private void setUserRoles(SignUpRequest signUpRequest, User user) {
        Set<String> assignRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (assignRoles == null) {
            Role userRole = roleRepository.findFirstByName(RoleEnum.ROLE_USER.getRole())
                    .orElseThrow(() -> new ApplicationException("Error: Role is not found.", HttpStatus.BAD_REQUEST.value()));
            roles.add(userRole);
        } else {
            assignRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findFirstByName(RoleEnum.ROLE_ADMIN.getRole())
                                .orElseThrow(() -> new ApplicationException("Error: Role is not found.", HttpStatus.BAD_REQUEST.value()));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findFirstByName(RoleEnum.ROLE_MODERATOR.getRole())
                                .orElseThrow(() -> new ApplicationException("Error: Role is not found.", HttpStatus.BAD_REQUEST.value()));
                        roles.add(modRole);

                        break;
                    //default: user
                    default:
                        Role userRole = roleRepository.findFirstByName(RoleEnum.ROLE_USER.getRole())
                                .orElseThrow(() -> new ApplicationException("Error: Role is not found.", HttpStatus.BAD_REQUEST.value()));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
    }
}
