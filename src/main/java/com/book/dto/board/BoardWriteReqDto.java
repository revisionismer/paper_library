package com.book.dto.board;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter @Setter
public class BoardWriteReqDto {

	@NotBlank
	private String title;
	
	@NotNull  // 공백만 허용.
	private String content;
	
	@JsonIgnore
	private MultipartFile thumnailFile;
	
	
	
	// toEntity 메소드 하나 추가해야함
}
