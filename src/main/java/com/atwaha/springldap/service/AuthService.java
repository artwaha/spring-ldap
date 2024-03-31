package com.atwaha.springldap.service;

import com.atwaha.springldap.Utils;
import com.atwaha.springldap.model.User;
import com.atwaha.springldap.model.dto.AuthResponse;
import com.atwaha.springldap.model.dto.LoginRequest;
import com.atwaha.springldap.repository.UserRepository;
import com.atwaha.springldap.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final ActiveDirectoryLdapAuthenticationProvider authenticationManager;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final Utils utils;

    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        String accessToken = jwtService.generateToken(user);

        utils.revokeAllUserTokens(user);
        utils.saveUserToken(accessToken, user);

        return AuthResponse
                .builder()
                .token(accessToken)
                .build();
    }
}
