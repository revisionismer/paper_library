package com.book.dto.book;

import com.book.domain.book.Book;
import com.book.domain.visit.Visit;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class BookRespDto {

	private Long id;
	private String title;
	private String author;
	
	private Long visitCnt;
	
	public BookRespDto() {}
	
	public BookRespDto(Long id, String title, String author) {
		this.id = id;
		this.title = title;
		this.author = title;
	}
	
	public BookRespDto(Book bookEntity) {
		this.id = bookEntity.getId();
		this.title = bookEntity.getTitle();
		this.author = bookEntity.getAuthor();
	}
	
	public BookRespDto(Book bookEntity, Visit visitEntity) {
		this.id = bookEntity.getId();
		this.title = bookEntity.getTitle();
		this.author = bookEntity.getAuthor();
		this.visitCnt = visitEntity.getTotalCount();
	}
}
