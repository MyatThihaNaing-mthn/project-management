package com.th.pm.model;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "first_name", nullable = false)
    @EqualsAndHashCode.Include
    private String firstname;

    @Column(name = "last_name", nullable = false)
    @EqualsAndHashCode.Include
    private String lastname;

    @Column(name = "email", nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "profile_image")
    private String profileImageUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @ManyToMany
    @JoinTable(
        name = "user_workspace",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "workspace_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "workspace_id"})
    )
    private Set<WorkSpace> belongedWorkspaces;

    @OneToMany(mappedBy = "createdBy")
    private Set<WorkSpace> creatredWorkSpaces;

    @ManyToMany
    @JoinTable(
        name = "user_board",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "board_id")
    )
    private Set<Board> boards;

    @ManyToMany(mappedBy = "assignees")
    private Set<Task> assignedTask;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Token> refreshTokens;

   

    public void addToken(Token token){
        this.refreshTokens.add(token);
    }

    public void removeToken(Token tokenToRemove){
        for(int i=0; i<refreshTokens.size(); i++){
            if(refreshTokens.get(i).equals(tokenToRemove)){
                refreshTokens.remove(i);
            }
        }
    }
}
