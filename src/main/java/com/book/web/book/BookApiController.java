package com.book.web.book;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.book.config.auth.PrincipalDetails;
import com.book.dto.ResponseDto;
import com.book.dto.book.BookReqDto;
import com.book.dto.book.BookRespDto;
import com.book.service.book.BookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class BookApiController {
	
	private final BookService bookService;
	
	@PostMapping("/s/save")
	public ResponseEntity<?> save(@RequestBody @Valid final BookReqDto bookReqDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		BookRespDto bookRespDto = bookService.saveBook(bookReqDto, principalDetails.getUser());
		
		return new ResponseEntity<>(new ResponseDto<>(1, "책 등록 성공", bookRespDto), HttpStatus.CREATED);
	}

	@GetMapping("/all")
	public ResponseEntity<?> findAll() {
		
		List<BookRespDto> result = bookService.findAll();
		
		return new ResponseEntity<>(new ResponseDto<>(1, "책 리스트 조회 성공", result), HttpStatus.OK);
	}
	
	@GetMapping("/s/{bookId}/detail")
	public ResponseEntity<?> findOnebyBookId(@PathVariable("bookId") Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		BookRespDto result = bookService.findOneByBookId(id, principalDetails.getUser());
		
		return new ResponseEntity<>(new ResponseDto<>(1, id + "번 책 정보 조회 성공", result), HttpStatus.OK);
	}
	
	@PutMapping("/s/{bookId}/update")
	public ResponseEntity<?> updateOnebyBookId(@RequestBody @Valid final BookReqDto bookUpdateReqDto, @PathVariable("bookId") Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		BookRespDto result = bookService.updateOneByBookId(id, bookUpdateReqDto, principalDetails.getUser());
		
		return new ResponseEntity<>(new ResponseDto<>(1, id + "번 책 정보 수정 성공", result), HttpStatus.OK);
	}
	
	@DeleteMapping("/s/{bookId}/delete")
	public ResponseEntity<?> deleteOnebyBookId(@PathVariable("bookId") Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		bookService.deleteOneByBookId(id, principalDetails.getUser());
		
		return new ResponseEntity<>(new ResponseDto<>(1, id + "번 책 정보 삭제 성공", null), HttpStatus.OK);
	}
}
