package com.book.domain.board;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long>{
	
	List<BoardFile> findByBoardId(Long boardId);
	
	Optional<BoardFile> findByBoardIdAndUserId(Long boardId, Long userId);
}
