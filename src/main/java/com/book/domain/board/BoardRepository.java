package com.book.domain.board;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long>{
	/**
	 *  1-1. 게시글 리스트 조회 - (삭제 여부 필터링) 
	 */
	List<Board> findAllByDeleteYn(final char deleteYn, final Sort sort);
	
	/**
	 *  1-2. 게시글 리스트 조회 - (페이징) 
	 */
	@Query(value = "SELECT * FROM board_tb ORDER BY id DESC", nativeQuery = true)
	Page<Board> findAllBoard(Pageable pageable);
}

