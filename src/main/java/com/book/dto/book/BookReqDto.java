package com.book.dto.book;

import com.book.domain.book.Book;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookReqDto {

	// 2024-03-27 : \s : 공백 허용, . : 모든 문자 허용
	@Pattern(regexp = "^[a-zA-Z0-9가-힣\s.]{2,20}$", message = "한글 영문과 숫자를 조합해서 2~20자 이내로 작성해주세요.")
	@NotEmpty  // 1-1. null이거나 공백일 수 없다.
	private String title;
	
	@Pattern(regexp = "^[a-zA-Z0-9가-힣\s.]{2,20}$", message = "한글 영문과 숫자를 조합해서 2~20자 이내로 작성해주세요.") 
	@NotEmpty
	private String author; 
	
	public Book toEntity() {
		return Book.builder()
				.title(title)
				.author(author)
				.build();
	}
}
