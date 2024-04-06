package com.book.domain.book;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "book_tb")
@Entity
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	
	private String author;
	
	@JoinColumn(name = "userId")
    @ManyToOne
    private User user;
	
	@CreatedDate
 	@Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
 	private LocalDateTime createdAt;
 	 
 	@LastModifiedDate
 	@Column(nullable = true)
 	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
 	private LocalDateTime updatedAt;  

}
