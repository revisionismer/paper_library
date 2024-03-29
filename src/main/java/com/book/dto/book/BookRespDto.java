package com.book.dto.book;

import com.book.domain.book.Book;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class BookRespDto {

	private Long id;
	private String title;
	private String author;
	
	public BookRespDto() {
		// TODO Auto-generated constructor stub
	}
	
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
}
