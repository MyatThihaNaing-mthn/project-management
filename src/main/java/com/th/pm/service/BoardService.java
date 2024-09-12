package com.th.pm.service;

import com.th.pm.dto.BoardDto;
import com.th.pm.dto.BoardRequest;
import com.th.pm.dto.BoardStatus;

public interface BoardService {
    BoardDto createBoard(BoardRequest projectRequest, String userId);
    BoardDto updateBoardStatus(BoardStatus status);
    BoardDto findBoardById(String id);
}
