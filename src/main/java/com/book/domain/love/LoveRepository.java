package com.book.domain.love;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoveRepository extends JpaRepository<Love, Long> {

	@Query(value = "SELECT * FROM love_tb WHERE pageOwnerId = :pageOwnerId AND userId = :userId AND boardId = :boardId", nativeQuery = true)
    Optional<Love> mFindLoveByPageOwnerIdAndUserIdAndBoardId(@Param("pageOwnerId") Long pageOwnerId, @Param("userId") Long userId, @Param("boardId") Long boardId);
	
	Long countByboardId(Long boardId);
}
