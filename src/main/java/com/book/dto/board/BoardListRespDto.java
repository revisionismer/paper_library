package com.book.dto.board;

import java.util.List;

import org.springframework.data.domain.Page;

import com.book.domain.board.Board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
public class BoardListRespDto {

	private Page<Board> boards;

    private Integer prev;
    private Integer next;
    private List<Integer> pageNumbers;
    private Long totalCount;
    private Boolean isPrev;
    private Boolean isNext;
    
    private Boolean isFirst;
}
