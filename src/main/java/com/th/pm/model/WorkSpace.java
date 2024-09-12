package com.th.pm.model;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "workspaces")
@Data
public class WorkSpace {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToMany(mappedBy = "belongedWorkspaces", fetch = FetchType.LAZY)
    @Column(name = "members")
    private Set<User> members;

    @OneToMany(mappedBy = "workspace")
    private Set<Board> boards;
}
