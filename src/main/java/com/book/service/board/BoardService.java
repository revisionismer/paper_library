package com.book.service.board;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.book.constant.user.UserEnum;
import com.book.domain.board.Board;
import com.book.domain.board.BoardFile;
import com.book.domain.board.BoardFileRepository;
import com.book.domain.board.BoardRepository;
import com.book.domain.user.User;
import com.book.dto.board.BoardListRespDto;
import com.book.dto.board.BoardWriteRespDto;
import com.book.handler.exception.CustomApiException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)  // 8-8. RuntimeException 말고도 모든 예외가 터졌을시 롤백시킨다.
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;
	private final BoardFileRepository boardFileRepository;
	private final FileService fileService;

	public BoardWriteRespDto writeBoard(String boardString, MultipartFile[] files, User loginUser) throws Exception {
		
		if(loginUser.getRole() == UserEnum.ADMIN || loginUser.getRole() == UserEnum.USER) {
			
			Board board = new ObjectMapper().readValue(boardString, Board.class);
			board.setUserId(loginUser.getId());
			board.setWriter(loginUser.getUsername());
			board.setDeleteYn('N');
			board.setCreatedAt(LocalDateTime.now());
			
			Board newBoard = boardRepository.save(board);
			
			BoardFile boardFile = null;
			
			if(files != null) {	
				boardFile = new BoardFile();
			
				for(MultipartFile file : files) {
					boardFile = new BoardFile();
					
					// 파일 저장
					try {
						String createdFilename = fileService.uploadFile(file.getOriginalFilename(), file.getBytes());
						
						// 파일 정보 저장
						boardFile.setFileName(file.getOriginalFilename());
						boardFile.setFileUrl(createdFilename);
						boardFile.setDownCnt(0);
						boardFile.setBoardId(newBoard.getId());
						boardFile.setUserId(loginUser.getId());
						boardFile.setCreatedAt(LocalDateTime.now());
						
						newBoard.imageUpdate(file.getOriginalFilename(), createdFilename);
				
						boardFileRepository.save(boardFile);
						
					} catch (MaxUploadSizeExceededException e) {
						throw new CustomApiException(e.getMessage());
					} catch (SizeLimitExceededException e) {
						throw new CustomApiException(e.getMessage());
					}
					
				}
			}
			
			if(boardFile == null) {
				return new BoardWriteRespDto(newBoard);
			} else {
				return new BoardWriteRespDto(newBoard, boardFile);
			}
		} else {
			throw new CustomApiException("글을 작성할 수 있는 권한이 있는 유저가 아닙니다.");
		}
	}
	
	@Transactional(readOnly = true)
	public BoardListRespDto readBoardList(Pageable pageable) {
		
		Page<Board> boards = boardRepository.findAll(pageable);
		
		List<Integer> pageNumbers = new ArrayList<>();
		
		for (int i = 0; i < boards.getTotalPages(); i++) {
			pageNumbers.add(i + 1);
	    }
		
		BoardListRespDto postListRespDto = new BoardListRespDto(
				boards, 
				boards.getNumber() - 1, 
				boards.getNumber() + 1, 
				pageNumbers, 
				null,
				(boards.getNumber() - 1) != -1 ? true : false, 
		        (boards.getNumber() + 1) != boards.getTotalPages() ? true : false, 
		        (boards.getContent().size() == 0) ? true : false
		);
        		
		return postListRespDto;
		
	}
	
}
