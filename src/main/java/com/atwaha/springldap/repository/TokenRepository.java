package com.atwaha.springldap.repository;

import com.atwaha.springldap.model.Token;
import com.atwaha.springldap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {
    boolean existsByTokenAndRevokedTrue(String token);

    List<Token> findByUserAndRevokedFalse(User user);

    Token findByToken(String token);
}