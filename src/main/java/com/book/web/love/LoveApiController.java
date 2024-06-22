package com.book.web.love;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.book.config.auth.PrincipalDetails;
import com.book.domain.user.User;
import com.book.dto.ResponseDto;
import com.book.dto.love.LoveRespDto;
import com.book.service.love.LoveService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loves")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class LoveApiController {
	
	private final LoveService loveService; 

	@PostMapping("/s/{boardId}/love")
	public ResponseEntity<?> loveBoard(@PathVariable("boardId") Long boardId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User loginUser = principalDetails.getUser();
		
		LoveRespDto loveRespDto = loveService.loveBoard(boardId, loginUser);
		
		return new ResponseEntity<>(new ResponseDto<>(1, "게시글 좋아요 성공", loveRespDto), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/s/{boardId}/unlove")
	public ResponseEntity<?> unloveBoard(@PathVariable("boardId") Long boardId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User loginUser = principalDetails.getUser();
		
		LoveRespDto loveRespDto = loveService.unLoveBoard(boardId, loginUser);
		
		return new ResponseEntity<>(new ResponseDto<>(1, "게시글 좋아요 취소 성공", loveRespDto), HttpStatus.OK);
	}
}
