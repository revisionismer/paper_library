package com.book.dto.board.paging;

import lombok.Getter;

@Getter
public class Pagination {
	// 10-1. 전체 데이터 갯수
	private int totalRecordCount;

	// 10-2. 전체 페이지 수
	private int totalPageCount;

	// 10-3. 첫번째 페이지 번호
	private int startPage;

	// 10-4. 끝 페이지 번호
	private int endPage;

	// 10-5. LIMIT 시작 위치
	private int limitStart;

	// 10-6. 이전 페이지 존재 여부
	private boolean existPrevPage;

	// 10-7. 다음 페이지 존재 여부
	private boolean existNextPage;
	
	// 10-8. 전체 페이지 수 계산해주는 메소드
	private void calculation(CommonParams params) {
		// 10-9. 전체 페이지 수 계산
		totalPageCount = ( (totalRecordCount - 1) / params.getRecordPerPage() ) + 1;
				
		// 10-10. 현재 페이지 번호가 전체 페이지 수보다 큰 경우, 현재 페이지 번호에 전체 페이지 수 저장
		if(params.getPage() > totalPageCount) {
			params.setPage(totalPageCount);
		}
		
		// 10-11. 첫 페이지 번호 계산(ex)하단 페이지 번호 5개)
		// ( (1 - 1) / 5 ) * 5 + 1 => 0 * 5 + 1 = 1
		// ( (2 - 1) / 5 ) * 5 + 1 => 1 * 5 + 1 = 6  
		// ( (3 - 1) / 5 ) * 5 + 1 => 2 * 5 + 1 = 11
		// ( (4 - 1) / 5 ) * 5 + 1 => 3 * 5 + 1 = 16
		// ( (5 - 1) / 5 ) * 5 + 1 => 4 * 5 + 1 = 21
		startPage = ( (params.getPage() - 1) / params.getPageSize() ) * params.getPageSize() + 1;
		
		// 10-12. 끝 페이지 번호 계산(ex)하단 페이지 번호 5개)
		//  1 + 5 - 1 = 5
		//  6 + 5 - 1 = 10
		// 11 + 5 - 1 = 15
		// 16 + 5 - 1 = 20
		// 21 + 5 - 1 = 25
		endPage = startPage + params.getPageSize() - 1;
				
		// 10-13. 끝 페이지가 전체 페이지 수보다 큰 경우, 끝 페이지 번호에 전체 페이지 수 저장.
		if(endPage > totalPageCount) {
			endPage = totalPageCount;
		}
		
		// 10-14. LIMIT 시작 위치 계산(ex)한 페이지당 게시글 10개씩) : 나머지는 버린다.
		// ( 1 - 1 ) * 10 = 0
		// ( 2 - 1 ) * 10 = 0.1
		// ( 3 - 1 ) * 10 = 0.3
		// ( 4 - 1 ) * 10 = 0.4
		//         ......
		// ( 11 - 1 ) * 10 = 1
		// ( 12 - 1 ) * 10 = 1.1
		// ( 13 - 1 ) * 10 = 1.2
		limitStart = (params.getPage() - 1) * params.getRecordPerPage();
		
		// 10-15. 이전 페이지 존재 여부 확인 : startPage -> params.getPage() -> 2023-12-12 수정
		existPrevPage = (params.getPage() != 1);
		
		// 10-16. 다음 페이지 존재 여부 확인 : startPage -> params.getPage() -> 2023-12-12 수정(전체 게시글 수가 50개, 한 페이지당 10개씩 뿌려준다고 가정)
		// 1 * 10 < 50 = true
		// 2 * 10 < 50 = true
		// 3 * 10 < 50 = true
		// 4 * 10 < 50 = true
		// 5 * 10 < 50 = false
		existNextPage = (params.getPage() * params.getRecordPerPage()) < totalRecordCount;
		
	}
	
	// 10-17. 생성자
	public Pagination(int totalRecordCount, CommonParams params) {
		if(totalRecordCount > 0) {
			this.totalRecordCount = totalRecordCount;
			this.calculation(params);
		}
	}
	
}
