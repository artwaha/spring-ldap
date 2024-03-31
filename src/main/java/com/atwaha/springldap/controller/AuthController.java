package com.atwaha.springldap.controller;

import com.atwaha.springldap.model.dto.LoginRequest;
import com.atwaha.springldap.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping
    public String index() {
        return "Welcome to Spring LDAP Authentication!";
    }

    @PostMapping("login")
    public ResponseEntity<LdapUserDetails> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
}
