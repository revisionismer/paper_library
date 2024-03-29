package com.book.domain.book;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long>{

	// 1-1. JPA Query Method
	Optional<Book> findByTitle(String title);
}
