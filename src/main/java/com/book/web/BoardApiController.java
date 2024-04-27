package com.book.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.book.config.auth.PrincipalDetails;
import com.book.domain.user.User;
import com.book.dto.ResponseDto;
import com.book.dto.board.BoardListRespDto;
import com.book.dto.board.BoardWriteRespDto;
import com.book.service.board.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class BoardApiController {

	@Value("${board.path}")
	private String uploadFolder;
	
	private final BoardService boardService;

//	@PostMapping("/write")
	@PostMapping("/s/write")
	public ResponseEntity<?> writeBoard(@RequestPart("board") String boardString, @RequestPart(value = "files", required = false) MultipartFile[] files, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
		
		User loginUser = principalDetails.getUser();
		
		BoardWriteRespDto result = boardService.writeBoard(boardString, files, loginUser);
		
		return new ResponseEntity<>(new ResponseDto<>(1, "게시글 글쓰기 성공", result), HttpStatus.CREATED);
	}
	
	@GetMapping("")
	public ResponseEntity<?> readAllboard(Pageable pageable) {
	
		BoardListRespDto boardListRespDto = boardService.readBoardList(pageable);
		
		return new ResponseEntity<>(new ResponseDto<>(1, "게시글 리스트 불러오기 성공", boardListRespDto), HttpStatus.OK);
	}
}
