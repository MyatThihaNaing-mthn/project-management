package com.th.pm.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.th.pm.model.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID>{
    Optional<Token> findByToken(String token);
}
