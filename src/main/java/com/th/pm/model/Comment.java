package com.th.pm.model;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "comments")
@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "posted_by", nullable = false, updatable = false)
    private User postedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    
    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false, updatable = false)
    private Task task;
}
