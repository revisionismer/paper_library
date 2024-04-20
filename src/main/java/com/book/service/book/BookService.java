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
import com.book.domain.visit.Visit;
import com.book.domain.visit.VisitRepository;
import com.book.dto.book.BookReqDto;
import com.book.dto.book.BookRespDto;
import com.book.handler.exception.CustomApiException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

	private final BookRepository bookRepository;
	private final VisitRepository visitRepository;
	
	/**
	 * 1-1. 책 저장하기 서비스
	 * 
	 **/
	public BookRespDto saveBook(final BookReqDto bookReqDto, User loginUser) {
		
		if(loginUser.getRole() == UserEnum.ADMIN) {
			Book bookEntity = new Book();
			bookEntity.setTitle(bookReqDto.getTitle());
			bookEntity.setAuthor(bookReqDto.getAuthor());
			bookEntity.setUser(loginUser);
			bookEntity.setCreatedAt(LocalDateTime.now());
			
			Book newBook = bookRepository.save(bookEntity);
			
			Visit visit = new Visit(0L, loginUser, newBook);
			
			visitRepository.save(visit);
			
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
			// 1-8. 리스트 조회때 방문객 수도 가져오게 변경
			Visit findVisit = visitRepository.findByUserIdAndBookId(entity.getUser().getId(), entity.getId());
			
			result.add(new BookRespDto(entity, findVisit));
		}
		
		return result;
		
	}
	
	/**
	 * 1-3. 책 한건 조회 서비스 : 조회수 처리 완료 -> 2024-04-05
	 * 
	 **/
	public BookRespDto findOneByBookId(Long bookId, User loginUser) {
		
		Optional<Book> bookOp = bookRepository.findById(bookId);
		
		if(bookOp.isPresent()) {
			Book findBook = bookOp.get();
			
			Long pageOwnerId = findBook.getUser().getId();
			Long principalId = loginUser.getId();
			
			// 주의 : 페이지 주인의 Id랑 로그인유저의 Id랑 같을때 조회수가 올라가도록 로직 구성 : 나중에 조건문 수정 요망.
			visitIncrease(pageOwnerId, principalId, findBook.getId());
			
			Visit findVisit = visitRepository.findByUserIdAndBookId(pageOwnerId, bookId);
			
			return new BookRespDto(findBook, findVisit);
		} else {
			throw new CustomApiException("해당 도서가 존재하지 않습니다.");
		}
		
	}
	
	/**
	 * 1-4. 책 정보 수정 서비스 
	 * 
	 **/
	public BookRespDto updateOneByBookId(Long bookId, BookReqDto bookUpdateReqDto, User loginUser) {
		
		Optional<Book> bookOp = bookRepository.findById(bookId);
		
		if(bookOp.isPresent()) {
			
			Book findBook = bookOp.get();
			findBook.setTitle(bookUpdateReqDto.getTitle());
			findBook.setAuthor(bookUpdateReqDto.getAuthor());
			findBook.setUpdatedAt(LocalDateTime.now());
			
			// 2024-04-18 : 작성자 본인만 수정 가능 추가 
			Long loginUserId = loginUser.getId();
			Long saveBookUserId = findBook.getUser().getId();
			
			if(loginUserId == saveBookUserId) {
				Book updatedBook = bookRepository.save(findBook);
				
				return new BookRespDto(updatedBook);
			} else {
				throw new CustomApiException("본인 글이 아니면 수정할 수 없습니다.");
			}
			
			
		} else {
			throw new CustomApiException("해당 도서가 존재하지 않습니다.");
		}
	}
	
	/**
	 * 1-5. 책 정보 삭제 서비스
	 * 
	 **/
	public void deleteOneByBookId(Long bookId, User loginUser) {
		
		Optional<Book> bookOp = bookRepository.findById(bookId);
		
		if(bookOp.isPresent()) {
			Book findBook = bookOp.get();
			
			// 2024-04-18 : 작성자 본인만 삭제 가능 추가
			Long loginUserId = loginUser.getId();
			Long saveBookUserId = findBook.getUser().getId();
			
			if(loginUserId == saveBookUserId) {

				// 1-6. 책 엔티티와 연관관계 매핑된 방문객 엔티티부터 삭제하고
				Visit visit = visitRepository.findByUserIdAndBookId(findBook.getUser().getId(), bookId);
				visitRepository.delete(visit);
				
				// 1-7. 1-6이 정상적으로 삭제되면 삭제
				bookRepository.deleteById(findBook.getId());
			} else {
				throw new CustomApiException("본인 글이 아니면 삭제할 수 없습니다.");
			}
			
		} else {
			throw new CustomApiException("해당 도서를 삭제하는데 실패하였습니다.");
		}
	}
	
	/**
	 * 2-1. 조회 수 증가 서비스(더티 체킹)
	 * 
	 **/
	private Visit visitIncrease(Long pageOwnerId, Long principalId, Long bookId) {	
		// 2-2. 회원가입시 만들어진 방문 엔티티를 페이지 주인의 id로 찾아온다.
        Visit visitEntity = visitRepository.findByUserIdAndBookId(pageOwnerId, bookId);
        
        // 2-3. 전체 방문수를 가져온다.
        Long totalCount = visitEntity.getTotalCount();
            
        // 2-6. 페이지주인의 아이디와 로그인한 회원의 아이디가 다를때만
        if(!pageOwnerId.equals(principalId)) {
            // 2-7. 방문자 수를 1씩 증가시켜준다.
            visitEntity.setTotalCount(totalCount + 1L);
        }
            
        return visitEntity;
	}
	
	
}
