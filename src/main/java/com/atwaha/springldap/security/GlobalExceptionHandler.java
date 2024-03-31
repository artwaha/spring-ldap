package com.atwaha.springldap.security;

import com.atwaha.springldap.Utils;
import com.atwaha.springldap.model.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final Utils utils;
    private final HttpServletRequest request;

    /* TODO: Add AuthenticationException for when auth fails due to bad credentials*/
    /* TODO: Add CommunicationException for when there is problem reaching the LDAP Server*/
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleOtherExceptions(Exception exception) {
        ErrorResponseDTO response = utils.generateErrorResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(), exception.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
