package com.th.pm.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.th.pm.model.User;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, UUID>{
    Optional<User> findByEmail(String email);
}
