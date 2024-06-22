package com.book.service.love;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.book.domain.board.Board;
import com.book.domain.board.BoardRepository;
import com.book.domain.love.Love;
import com.book.domain.love.LoveRepository;
import com.book.domain.user.User;
import com.book.domain.user.UserRepository;
import com.book.dto.love.LoveRespDto;
import com.book.handler.exception.CustomApiException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)  // 8-8. RuntimeException 말고도 모든 예외가 터졌을시 롤백시킨다.
@RequiredArgsConstructor
public class LoveService {

	private final LoveRepository loveRepository;
	private final BoardRepository boardRepository;
	private final UserRepository userRepository;
	
	public LoveRespDto loveBoard(Long boardId, User loginUser) {
	
		Optional<Board> boardOp = boardRepository.findById(boardId);
		
		if(boardOp.isPresent()) {
			Board findBoard = boardOp.get();
			
			Optional<Love> loveOp = loveRepository.mFindLoveByPageOwnerIdAndUserIdAndBoardId(findBoard.getUserId(), loginUser.getId(), findBoard.getId());
			
			User pageOwner = userRepository.findUserById(findBoard.getUserId()); 
	
			if(!loveOp.isPresent()) {
				Love newLove = new Love();
				
				newLove.setBoard(findBoard);
				newLove.setUser(loginUser);
				newLove.setPageOwner(pageOwner);
				newLove.setCreatedAt(LocalDateTime.now());
		
				Love loveEntity = loveRepository.save(newLove);
				
				Long totalLoveCnt = loveRepository.countByboardId(boardId);
				
				return new LoveRespDto(loveEntity.getId(), findBoard.getId(), loginUser.getId(), pageOwner.getId(), true, totalLoveCnt);
				
			} else {
				throw new CustomApiException("이미 좋아요를 했습니다.");
			}
		} else {
			throw new CustomApiException("게시글이 존재하지 않습니다.");
			
		}
		
	}
	
	public LoveRespDto unLoveBoard(Long boardId, User loginUser) {
		
		Optional<Board> boardOp = boardRepository.findById(boardId);
		
		if(boardOp.isPresent()) {
			Board findBoard = boardOp.get();
			
			Optional<Love> loveOp = loveRepository.mFindLoveByPageOwnerIdAndUserIdAndBoardId(findBoard.getUserId(), loginUser.getId(), findBoard.getId());
			
			User pageOwner = userRepository.findUserById(findBoard.getUserId()); 
			
			if(loveOp.isPresent()) {
				
				Love findLove = loveOp.get();
				
				loveRepository.delete(findLove);
				
				Long totalLoveCnt = loveRepository.countByboardId(boardId);
				
				return new LoveRespDto(findLove.getId(), findBoard.getId(), loginUser.getId(), pageOwner.getId(), false, totalLoveCnt);
				
			} else {
				throw new CustomApiException("좋아요를 하지 않은 상태입니다.");
			}
		}
		
		throw new CustomApiException("게시글이 존재하지 않습니다."); 

	}
}
