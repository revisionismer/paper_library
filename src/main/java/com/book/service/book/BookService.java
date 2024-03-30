package com.book.service.book;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
// import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.book.constant.user.UserEnum;
import com.book.domain.book.Book;
import com.book.domain.book.BookRepository;
import com.book.domain.user.User;
import com.book.dto.book.BookReqDto;
import com.book.dto.book.BookRespDto;
import com.book.handler.exception.CustomApiException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

	private final BookRepository bookRepository;
	
	/**
	 * 1-1. 책 저장하기 서비스
	 * 
	 **/
	public BookRespDto saveBook(final BookReqDto bookReqDto, User loginUser) {
		
		if(loginUser.getRole() == UserEnum.ADMIN) {
			Book bookEntity = new Book();
			bookEntity.setTitle(bookReqDto.getTitle());
			bookEntity.setAuthor(bookReqDto.getAuthor());
			bookEntity.setCreatedAt(LocalDateTime.now());
			
			Book newBook = bookRepository.save(bookEntity);
			
			return new BookRespDto(newBook);
		} else {
			throw new CustomApiException("도서를 저장할 수 있는 권한이 없는 사용자입니다.");
		}
		
	}
	
	/**
	 * 1-2. 책 조회 서비스
	 * 
	 **/
	@Transactional(readOnly = true)
	public List<BookRespDto> findAll() {
		
		Sort sort = Sort.by(Direction.DESC, "id", "createdAt");
		
		List<Book> bookList = bookRepository.findAll(sort);
		
//		return bookList.stream().map(BookRespDto::new).collect(Collectors.toList());
		
		List<BookRespDto> result = new ArrayList<>();
		
		for(Book entity : bookList) {
			result.add(new BookRespDto(entity));
		}
		
		return result;
		
	}
	
	/**
	 * 1-3. 책 한건 조회 서비스
	 * 
	 **/
	public BookRespDto findOneByBookId(Long bookId , User loginUser) {
		
		Optional<Book> bookOp = bookRepository.findById(bookId);
		
		if(bookOp.isPresent()) {
			Book findBook = bookOp.get();
			
			return new BookRespDto(findBook);
		} else {
			throw new CustomApiException("해당 도서가 존재하지 않습니다.");
		}
		
	}
	
	
	
}
