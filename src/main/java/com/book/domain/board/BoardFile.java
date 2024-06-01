package com.book.domain.board;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
@Table(name = "boardFile_tb")
@Entity
public class BoardFile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String fileName;
	
	private String fileUrl;
	
	private Long userId;
	
	private Long boardId;
	
	private Integer downCnt;
	
	@CreatedDate
 	@Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
 	private LocalDateTime createdAt; 
 	 
 	@LastModifiedDate
 	@Column(nullable = true)
 	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
 	private LocalDateTime updatedAt;  
 	
 	public void createBoardFile(MultipartFile file, String createdFileName, Board updatedBoard) {
 		this.fileName = file.getOriginalFilename();
		this.fileUrl = createdFileName;
		this.downCnt = 0;
		this.userId = updatedBoard.getUserId();
		this.boardId =updatedBoard.getId();
	
		this.createdAt = LocalDateTime.now();
 	}
 	 	
 	public void updateBoardFile(MultipartFile file, String createdFileName, Board updatedBoard) {
 		this.fileName = file.getOriginalFilename();
		this.fileUrl = createdFileName;
		this.downCnt = 0;
		this.userId = updatedBoard.getUserId();
		this.boardId =updatedBoard.getId();
	
		this.updatedAt = LocalDateTime.now();
	
	}
}
