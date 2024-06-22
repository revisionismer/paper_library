package com.book.dto.board;

import java.time.LocalDateTime;

import com.book.domain.board.Board;
import com.book.domain.board.BoardFile;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
public class BoardInfoRespDto {

	private Long id;
	private String title;
	private String content;
	private String writer;
	private int hits;
	
	private String thumnailImgFileName;
	private String originalImgFileName;
	
	private boolean isPageOwner;
	
	private char deleteYn;
	
	private Long boardFileId;
	
	private boolean isLove;
	private Long totalLoveCnt;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdAt;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime updatedAt;
	
	public BoardInfoRespDto(Board board, BoardFile boardFile, boolean isPageOwner) {
		this.id = board.getId();
		this.title = board.getTitle();
		this.content = board.getContent();
		this.writer = board.getWriter();
		this.hits = board.getHits();
		this.deleteYn = board.getDeleteYn();
		
		this.thumnailImgFileName = board.getThumnailImgFileName();
		this.originalImgFileName = board.getOriginalImgFileName();
		
		this.isPageOwner = isPageOwner;
		
		this.boardFileId = boardFile.getId();
		
	}
	
	public BoardInfoRespDto(Board board, BoardFile boardFile, boolean isPageOwner, Boolean isLove, Long totalLoveCnt) {
		this.id = board.getId();
		this.title = board.getTitle();
		this.content = board.getContent();
		this.writer = board.getWriter();
		this.hits = board.getHits();
		this.deleteYn = board.getDeleteYn();
		
		this.thumnailImgFileName = board.getThumnailImgFileName();
		this.originalImgFileName = board.getOriginalImgFileName();
		
		this.isPageOwner = isPageOwner;
		
		this.boardFileId = boardFile.getId();
		
		this.isLove = isLove;
		this.totalLoveCnt = totalLoveCnt;
		
	}
	
	public BoardInfoRespDto(Board board) {
		this.id = board.getId();
		this.title = board.getTitle();
		this.content = board.getContent();
		this.writer = board.getWriter();
		this.hits = board.getHits();
		this.deleteYn = board.getDeleteYn();
		
	}
	
	public BoardInfoRespDto(Board board, BoardFile boardFile) {
		this.id = board.getId();
		this.title = board.getTitle();
		this.content = board.getContent();
		this.writer = board.getWriter();
		this.hits = board.getHits();
		this.deleteYn = board.getDeleteYn();
		
		this.boardFileId = boardFile.getId();
		this.thumnailImgFileName = boardFile.getFileName();
		this.originalImgFileName = boardFile.getFileUrl();
		
	}
}

// 2024-05-20 : 여기까지
