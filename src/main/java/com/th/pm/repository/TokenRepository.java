package com.th.pm.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.th.pm.model.Token;
import com.th.pm.model.User;

public interface TokenRepository extends JpaRepository<Token, UUID>{
    Optional<User> findUserByToken(String token);
}
