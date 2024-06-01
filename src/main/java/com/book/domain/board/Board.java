package com.book.domain.board;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
@Table(name = "board_tb")
@Entity
public class Board {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 5-1. PK
	
	private String title; // 5-2. 제목
	
	private String content; // 5-3. 내용
	
	private String writer; // 5-4. 작성자
	
	private int hits;  // 5-5. 조회수
	
	private char deleteYn; // 5-6. 삭제여부
	
	private Long userId;  // 5-7. 작성자 ID
	
	@Column(length = 200, nullable = true)
    private String thumnailImgFileName;
    
    @Column(length = 200, nullable = true)
    private String originalImgFileName;
	
	@CreatedDate
 	@Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
 	private LocalDateTime createdAt;  // 5-7.
 	 
 	@LastModifiedDate
 	@Column(nullable = true)
 	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
 	private LocalDateTime updatedAt;  // 5-8.
	
	/**
	 *  5-9. 엔티티로 변환해주는 생성자 빌더.
	 */
	@Builder
	public Board(String title, String content, String writer, int hits, char deleteYn) {
		this.title = title;
		this.content = content;
		this.writer = writer;
		this.hits = hits;
		this.deleteYn = deleteYn;
	}
	
	/**
	 * 5-10. 게시글 수정 : 변경 감지
	 */
	public void update(String title, String content) {
		this.title = title;
		this.content = content;
		this.updatedAt = LocalDateTime.now();		
	}
	
	/**
	 * 5-11. 조회수 증가 : 변경 감지
	 */ 
	public void increaseHits() {
		this.hits++;
	}
	
	/**
	 * 5-12. 게시글 삭제 : 변경 감지
	 */
	public void delete() {
		this.deleteYn = 'Y';
	}
	
	/**
	 * 5-13. 게시글 이미지 업데이트 : 변경 감지
	 **/
	public void imageUpdate(String originalFilename, String thumnailImgFilename) {
		this.originalImgFileName = originalFilename;
		this.thumnailImgFileName = thumnailImgFilename;
		this.updatedAt = LocalDateTime.now();
	}
	
	/**
	 * 5-14. 게시글 수정(이미지 포함) : 변경 감지
	 * 
	 */
	public Board updateBoardWithImage(Board board) {
		this.title = board.getTitle();
		this.content = board.getContent();
		this.thumnailImgFileName = board.getThumnailImgFileName();
		this.originalImgFileName = board.getOriginalImgFileName();
		this.updatedAt = LocalDateTime.now();
		
		return this;
	}
	
	/**
	 * 5-14. 게시글 수정(이미지 포함) : 변경 감지
	 * 
	 */
	public Board updateBoard(Board board) {
		this.title = board.getTitle();
		this.content = board.getContent();
		this.updatedAt = LocalDateTime.now();
		
		return this;
	}
}
