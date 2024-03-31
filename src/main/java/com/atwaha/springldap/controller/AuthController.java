package com.atwaha.springldap.controller;

import com.atwaha.springldap.model.dto.AuthResponse;
import com.atwaha.springldap.model.dto.LoginRequest;
import com.atwaha.springldap.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @SecurityRequirement(name = "jba")
    @GetMapping("index")
    public String index() {
        return "Welcome to Spring LDAP Authentication!";
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
}
