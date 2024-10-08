package com.th.pm.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.th.pm.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID>{
    
}
