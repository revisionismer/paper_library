package com.book.domain.love;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.book.domain.board.Board;
import com.book.domain.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
@Table(
	name = "love_tb",
	uniqueConstraints = {
		@UniqueConstraint(name = "love_uk", columnNames = {"userId", "boardId", "pageOwnerId"})
	}
)
@Entity
public class Love {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;  // 1-1. PK
	
    @JoinColumn(name = "userId")
    @ManyToOne
    private User user;
    
    @JoinColumn(name = "boardId")
    @ManyToOne
    private Board board;
    
    @JoinColumn(name = "pageOwnerId")
    @ManyToOne
    private User pageOwner;
    
    @CreatedDate
 	@Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
 	private LocalDateTime createdAt;
 	 
 	@LastModifiedDate
 	@Column(nullable = true)
 	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
 	private LocalDateTime updatedAt; 
    
 
}
