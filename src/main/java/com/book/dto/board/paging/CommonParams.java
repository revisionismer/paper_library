package com.book.dto.board.paging;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommonParams {

	// 9-1. 현재 페이지 번호
	private int page;
	
	// 9-2. 페이지당 출력할 데이터 갯수
	private int recordPerPage;
	
	// 9-3. 화면 하단에 출력할 페이지 갯수
	private int pageSize;
	
	// 9-4. 검색 키워드
	private String keyword;
	
	// 9-5. 검색 조건
	private String searchType;
	
	// 9-6. 페이지네이션 객체
	private Pagination pagination;
}
