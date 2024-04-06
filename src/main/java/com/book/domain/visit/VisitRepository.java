package com.book.domain.visit;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, Long> {

	// 1-1. JPA Query Method
	Visit findByUserIdAndBookId(Long userId, Long bookId);
}