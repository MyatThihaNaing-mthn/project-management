package com.th.pm.model;

import java.util.UUID;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "token_value", nullable = false)
    private String token;

    @ManyToAny
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
