package com.th.pm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.th.pm.dto.BoardDto;
import com.th.pm.dto.BoardRequest;
import com.th.pm.dto.BoardStatus;
import com.th.pm.dto.UserDto;
import com.th.pm.security.UserDetailsImpl;
import com.th.pm.service.BoardService;
import com.th.pm.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/board")
@Slf4j
public class BoardController {

    @Autowired
    private BoardService boardService;
    @Autowired
    private UserService userService;

    @PostMapping("/creation")
    public ResponseEntity<BoardDto> createNewBoard(@RequestBody BoardRequest request) {
        String userId = getUserId();
        log.info("inside controller");
        BoardDto board = boardService.createBoard(request, userId);
        return new ResponseEntity<>(board, HttpStatus.CREATED);
    }

    @PostMapping("/status")
    public ResponseEntity<BoardDto> updateBoardStatus(@RequestBody BoardStatus status){
        //TODO validate object
        //TODO check ownership
        validateOwnership(getUserId(), status.getBoardId());
        BoardDto board = boardService.updateBoardStatus(status);
        return new ResponseEntity<>(board, HttpStatus.CREATED);
    }

    private String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            return userDetails.getId();
        }
        return null;
    }

    private void validateOwnership(String userId, String boardId){
        UserDto user = userService.findUserById(userId);
        BoardDto board = boardService.findBoardById(boardId);
        if(!user.getId().equals(board.getCreatedBy().getId())){
            throw new AccessDeniedException("You are not authorized to perform this ops");
        }
    }

}
