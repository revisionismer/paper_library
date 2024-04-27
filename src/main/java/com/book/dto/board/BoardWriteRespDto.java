package com.book.dto.board;

import java.time.LocalDateTime;

import com.book.domain.board.Board;
import com.book.domain.board.BoardFile;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardWriteRespDto {

	private Long id; // 1. PK
	
	private String title; // 2. 제목
	
	private String content; // 3. 내용
	
	private String writer; // 4. 작성자
	
	private int hits;  // 5. 조회수
	
	private char deleteYn; // 6. 삭제여부
	
	private Long boardFileId;
	
	private String oriFileName;
	
	private String fileUrl;
	
	private Long userId;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdAt;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime updatedAt;
	
	public BoardWriteRespDto(Board entity) {
		this.id = entity.getId();
		this.title = entity.getTitle();
		this.content = entity.getContent();
		this.writer = entity.getWriter();
		this.hits = entity.getHits();
		this.deleteYn = entity.getDeleteYn();
		this.userId = entity.getUserId();
		this.createdAt = entity.getCreatedAt();
		this.updatedAt = entity.getUpdatedAt();
	}
	
	public BoardWriteRespDto(Board entity, BoardFile boardFile) {
		this.id = entity.getId();
		this.title = entity.getTitle();
		this.content = entity.getContent();
		this.writer = entity.getWriter();
		this.hits = entity.getHits();
		this.deleteYn = entity.getDeleteYn();
		this.boardFileId = boardFile.getId();
		this.oriFileName = boardFile.getFileName();
		this.fileUrl = boardFile.getFileUrl();
		this.userId = entity.getUserId();
		this.createdAt = entity.getCreatedAt();
		this.updatedAt = entity.getUpdatedAt();
	}
}
