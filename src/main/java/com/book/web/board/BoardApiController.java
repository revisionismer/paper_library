package com.book.web.board;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.book.config.auth.PrincipalDetails;
import com.book.domain.user.User;
import com.book.dto.ResponseDto;
import com.book.dto.board.BoardInfoRespDto;
import com.book.dto.board.BoardListRespDto;
import com.book.dto.board.BoardPagingRespDto;
import com.book.dto.board.BoardWriteRespDto;
import com.book.dto.board.paging.CommonParams;
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
	
	@GetMapping("/s/{boardId}/detail")
	public ResponseEntity<?> readBoardByBoardId(@PathVariable("boardId") Long boardId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User loginUser = principalDetails.getUser();
		
		BoardInfoRespDto boardInfoRespDto = boardService.readBoardOne(boardId, loginUser);
		
		return new ResponseEntity<>(new ResponseDto<>(1, boardId + "번 게시글 정보 불러오기 성공", boardInfoRespDto), HttpStatus.OK);
	}
	
	@PutMapping("/s/{boardId}/update")
	public ResponseEntity<?> updateOnebyBookId(@PathVariable("boardId") Long boardId, @RequestPart("board") String boardString, @RequestPart(value = "files", required = false) MultipartFile[] files, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
		
		User loginUser = principalDetails.getUser();
		
		BoardInfoRespDto boardUpdateRespDto = boardService.updateOneByBoardId(boardId, boardString, files, loginUser);
		
		return new ResponseEntity<>(new ResponseDto<>(1, boardUpdateRespDto.getId() + "번 책 정보 수정 성공", boardUpdateRespDto), HttpStatus.OK);
	}

	// 호출할때 이런식으로 : http://localhost:8080/api/boards/all?page=1&recordPerPage=5&pageSize=5 
	@GetMapping("/all")  // 2024-05-30 : mybatis 의존성 3.0.3 으로 바꿔야함
	public ResponseEntity<?> readAllboardByPaging(final CommonParams params, @AuthenticationPrincipal PrincipalDetails principalDetails) { 
		
		BoardPagingRespDto boardPagingRespDto = boardService.findAllByPaging(params);
			
		return new ResponseEntity<>(new ResponseDto<>(1, "포스팅 글 리스트 불러오기 성공", boardPagingRespDto), HttpStatus.OK);
	}
	
	
}
