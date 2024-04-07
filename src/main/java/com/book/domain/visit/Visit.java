package com.book.domain.visit;

import java.time.LocalDateTime;

import com.book.domain.book.Book;
import com.book.domain.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Table(name = "visit_tb")
@Entity
public class Visit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;  // 1-1. PK
	
	@Column(nullable = true)
    private Long totalCount;

    @JoinColumn(name = "userId")
    @ManyToOne
    private User user;
    
    @JoinColumn(name = "bookId")
    @ManyToOne(cascade = CascadeType.ALL)
    private Book book;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updateAt;

    @PrePersist 
	public void createAt() {
		this.createAt = LocalDateTime.now();
	}

    @PreUpdate 
	public void updateAt() {
		this.updateAt = LocalDateTime.now();
	}
    
    @Builder
    public Visit(Long totalCount, User user, Book book) {
        this.totalCount = totalCount;
        this.user = user;
        this.book = book;
    }
    
}
