package com.book.mapper.board;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.book.dto.board.BoardInfoRespDto;
import com.book.dto.board.paging.CommonParams;

@Mapper
public interface IBoardMapper {
	/**
	 * 1. 게시글 수 조회  
	 */
	int count(final CommonParams params);
	
	/**
	 * 2. 게시글 리스트 조회(페이징) 
	 */
	List<BoardInfoRespDto> findAll(final CommonParams params);

}
