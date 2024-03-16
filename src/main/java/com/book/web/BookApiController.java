package com.book.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.book.config.auth.PrincipalDetails;
import com.book.dto.ResponseDto;
import com.book.dto.book.BookReqDto;
import com.book.dto.book.BookRespDto;
import com.book.service.book.BookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class BookApiController {
	
	private final BookService bookService;
	
	@PostMapping("/s/save")
	public ResponseEntity<?> save(final BookReqDto bookReqDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		BookRespDto bookRespDto = bookService.saveBook(bookReqDto, principalDetails.getUser());
		
		return new ResponseEntity<>(new ResponseDto<>(1, "책 등록 성공", bookRespDto), HttpStatus.CREATED);
	}

	@GetMapping("/all")
	public ResponseEntity<?> findAll() {
		
		List<BookRespDto> result = bookService.findAll();
		
		return new ResponseEntity<>(new ResponseDto<>(1, "책 리스트 조회 성공", result), HttpStatus.OK);
	}
}
