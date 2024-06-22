package com.book.dto.love;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
public class LoveRespDto {
	
	private Long loveId;

	private Long boardId;
	
	private Long userId;
	
	private Long pageOwnerId;
	
	private boolean isLove;
	
	private Long totalLoveCnt;
	
	public LoveRespDto(Long loveId, Long boardId, Long userId, Long pageOwnerId, Boolean isLove, Long totalLoveCnt) {
		this.loveId = loveId;
		this.boardId = boardId;
		this.userId = userId;
		this.pageOwnerId = pageOwnerId;
		this.isLove = isLove;
		this.totalLoveCnt = totalLoveCnt;
	}
}
