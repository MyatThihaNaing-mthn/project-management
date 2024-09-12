package com.th.pm.service.impl;


import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import com.th.pm.constant.Status;
import com.th.pm.dto.BoardDto;
import com.th.pm.dto.BoardRequest;
import com.th.pm.dto.BoardStatus;
import com.th.pm.exceptions.DatabaseException;
import com.th.pm.exceptions.EntityNotFoundException;
import com.th.pm.mapper.DtoMapper;
import com.th.pm.model.Board;
import com.th.pm.model.Task;
import com.th.pm.model.User;
import com.th.pm.repository.BoardRepository;
import com.th.pm.repository.UserRepository;
import com.th.pm.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BoardServiceImpl implements BoardService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public BoardDto createBoard(BoardRequest boardRequest, String userId) {
        Board board = new Board();

        board.setCreatedAt(Instant.now());
        board.setTitle(boardRequest.getTitle());
        
        Optional<User> user = userRepository.findById(UUID.fromString(userId));
        if(!user.isPresent()){
            throw new EntityNotFoundException("User not found");
        }

        board.setCreatedBy(user.get());
        board.setStatus(Status.CREATED);
        Set<User> members = new HashSet<>();
        members.add(user.get());
        board.setMembers(members);
        board.setTasks(new ArrayList<Task>());
        
        User updateUser = user.get();
        Set<Board> boards = updateUser.getBoards();
        boards.add(board);
        updateUser.setBoards(boards);

        try{
            Board savedboard = boardRepository.save(board);
            userRepository.save(updateUser);
            return DtoMapper.mapToBoardDto(savedboard);
        }catch(DataIntegrityViolationException e){
            log.info("Data Integrity Violation exception while saving board entity", e);
            throw new DatabaseException("Data Integrity Violation exception while saving board entity", e);
        }catch(JpaSystemException e){
            log.info("Jpa system exception while saving board entity", e);
            throw new DatabaseException("Jpa system exception while saving board entity", e);
        }
        
    }

    @Override
    public BoardDto updateBoardStatus(BoardStatus status) {
        String boardId = status.getBoardId();
        Board board = boardRepository.findById(UUID.fromString(boardId)).orElseThrow();
        Status boardStatus = Status.valueOf(status.getStatus());
        board.setStatus(boardStatus);
        Board savedboard = boardRepository.save(board);
        return DtoMapper.mapToBoardDto(savedboard);
    }

    @Override
    public BoardDto findBoardById(String id) {
        Board board = boardRepository.findById(UUID.fromString(id)).orElseThrow();
        return DtoMapper.mapToBoardDto(board);
    }

}
