package com.th.pm.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.th.pm.model.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, UUID>{
    
}
