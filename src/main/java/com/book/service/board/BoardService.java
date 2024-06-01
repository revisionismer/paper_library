package com.book.service.board;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.book.constant.user.UserEnum;
import com.book.domain.board.Board;
import com.book.domain.board.BoardFile;
import com.book.domain.board.BoardFileRepository;
import com.book.domain.board.BoardRepository;
import com.book.domain.user.User;
import com.book.dto.board.BoardInfoRespDto;
import com.book.dto.board.BoardListRespDto;
import com.book.dto.board.BoardPagingRespDto;
import com.book.dto.board.BoardWriteRespDto;
import com.book.dto.board.paging.CommonParams;
import com.book.dto.board.paging.Pagination;
import com.book.handler.exception.CustomApiException;
import com.book.mapper.board.IBoardMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)  // 8-8. RuntimeException 말고도 모든 예외가 터졌을시 롤백시킨다.
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;
	private final BoardFileRepository boardFileRepository;
	private final FileService fileService;
	
	private final IBoardMapper boardMapper; 

	// 3-1. 게시글 쓰기 서비스
	public BoardWriteRespDto writeBoard(String boardString, MultipartFile[] files, User loginUser) throws Exception {
		
		if(loginUser.getRole() == UserEnum.ADMIN || loginUser.getRole() == UserEnum.USER) {
			
			// 3-2. String으로 받아온걸 ObjectMapper로 board 엔티티로 변환하고 세부 항목 setting
			Board board = new ObjectMapper().readValue(boardString, Board.class);
			board.setUserId(loginUser.getId());
			board.setWriter(loginUser.getUsername());
			board.setDeleteYn('N');
			board.setCreatedAt(LocalDateTime.now());
			
			// 3-3. 영속화
			Board newBoard = boardRepository.save(board);
			
			// 3-4. boardFile
			BoardFile boardFile = null;
			
			// 3-5. 파일이 있을때
			if(files != null) {	
				
				// 3-6. new boardFile 
				boardFile = new BoardFile();
			
				for(MultipartFile file : files) {
					
					try {
						// 3-7. 파일 저장 
						String createdFilename = fileService.uploadFile(file.getOriginalFilename(), file.getBytes());
						
						// 3.8. 파일 정보 저장
						boardFile.setFileName(file.getOriginalFilename());
						boardFile.setFileUrl(createdFilename);
						boardFile.setDownCnt(0);
						boardFile.setBoardId(newBoard.getId());
						boardFile.setUserId(loginUser.getId());
						boardFile.setCreatedAt(LocalDateTime.now());
						
						// 3-9. board에 파일 정보 저장
						newBoard.imageUpdate(file.getOriginalFilename(), createdFilename);
				
						// 3-10. boardFile 영속화
						boardFileRepository.save(boardFile);
						
					} catch (MaxUploadSizeExceededException e) {
						throw new CustomApiException(e.getMessage());
					} catch (SizeLimitExceededException e) {
						throw new CustomApiException(e.getMessage());
					}
					
				}
			}
			
			if(boardFile == null) {
				return new BoardWriteRespDto(newBoard);
			} else {
				return new BoardWriteRespDto(newBoard, boardFile);
			}
		} else {
			throw new CustomApiException("글을 작성할 수 있는 권한이 있는 유저가 아닙니다.");
		}
	}
	
	@Transactional(readOnly = true)
	public BoardListRespDto readBoardList(Pageable pageable) { // 4-1. 게시글 List 서비스
		
		// 4-2. 게시글 가져오기(Pageable)
		Page<Board> boards = boardRepository.findAll(PageRequest.of(pageable.getPageNumber(), 2, Sort.by(Sort.Direction.DESC, "id")));
		
		// 4-3. 하단에 게시글 번호 동적 생성
		List<Integer> pageNumbers = new ArrayList<>();
		
		// 4-4. 게시글 수가 늘어나면 동적으로 페이지 번호가 생성되게 셋팅
		for (int i = 0; i < boards.getTotalPages(); i++) {
			pageNumbers.add(i + 1);
	    }
		
		// 4-5. BoardListRespDto dto에 게시글 페이징 데이터 싣기
		BoardListRespDto postListRespDto = new BoardListRespDto(
				boards, 
				boards.getNumber() - 1, 
				boards.getNumber() + 1, 
				pageNumbers, 
				boards.getContent().size(),
				(boards.getNumber() - 1) != -1 ? true : false, 
		        (boards.getNumber() + 1) != boards.getTotalPages() ? true : false, 
		        (boards.getContent().size() == 0) ? true : false
		);
        		
		// 4-6. return BoardListRespDto
		return postListRespDto;
		
	}
	
	// 5-1. 게시글 1건 읽기
	public BoardInfoRespDto readBoardOne(Long boardId, User loginUser) {
		
		Optional<Board> boardOp = boardRepository.findById(boardId);
		
		if(boardOp.isPresent()) {
			
			if(loginUser.getRole() == UserEnum.ADMIN || loginUser.getRole() == UserEnum.USER) {
				
				Board findBoard = boardOp.get();
				
				// 2024-05-21 : 본인이 아닐때만 조회수 증가
				if(findBoard.getUserId() != loginUser.getId()) {
					findBoard.increaseHits();
				}
				
				BoardFile boardFile = null;
				
				Optional<BoardFile> boardFileOp = boardFileRepository.findByBoardIdAndUserId(boardId, findBoard.getUserId());
				
				if(!boardFileOp.isPresent()) {
					boardFile = new BoardFile();
					
				} else {
					boardFile = boardFileOp.get();
				}
				
				return new BoardInfoRespDto(findBoard, boardFile, loginUser.getId() == findBoard.getUserId() ? true : false);
				
			} else {
				throw new CustomApiException("권한이 없는 사용자 입니다.");
			}
			
		} else {
			throw new CustomApiException("없는 게시글 입니다.");
		}
	}
	
	// 6-1. 게시글 수정하기 : 2024-05-31 -> 완료
	public BoardInfoRespDto updateOneByBoardId(Long boardId, String boardString, MultipartFile[] files, User loginUser) throws Exception {
		
		if(loginUser.getRole() == UserEnum.ADMIN || loginUser.getRole() == UserEnum.USER) {
			
			// 6-2. 전달된 boardId로 board 엔티티 가져온다.
			Board findBoard = boardRepository.findById(boardId).get();
			
			// 6-3. 전달된 boardString을 Board로 변환.
			Board board = new ObjectMapper().readValue(boardString, Board.class);

			// 6-4. 수정된 board 객체
			Board updatedBoard = null;
			
			// 6-5. boardFile
			BoardFile boardFile = null;
			
			// 6-6. DB에서 해당 유저가 올린 게시글에 BoardFile이 있는지 확인 하기 위해 DB에서 boardId와 userId로 해당 BoardFile을 가져온다.
			Optional<BoardFile> boardFileOp = boardFileRepository.findByBoardIdAndUserId(boardId, findBoard.getUserId());
			
			if(!boardFileOp.isPresent()) {  // 6-7. 업로드된 boardFile 정보가 없으면
				// 6-8. new boardFile : 새로운 boardFile 생성
				boardFile = new BoardFile();
		
			} else {
				// 6-9. 만약 기존에 올린 boardFile이 있으면 get 
				boardFile = boardFileOp.get();
			}
			
			// 6-10. 업로드할 파일이 있을때만 동작(기존 파일이 있어도 id가 thumnailFile인 파일 업로드 태그에 값이 없다면 없는거)
			if(files != null) {	
				
				// 6-11. boardId로 DB에서 찾아온 findBoard에 updatedBoard 셋팅
				updatedBoard = findBoard.updateBoardWithImage(board);
				
				for(MultipartFile file : files) {
					// 6-12. 파일 저장
					try {
						// 6-13. 실제 저장소에 파일 저장하고 생성된 uuid로 만들어진 createFilename을 얻어온다.
						String createdFilename = fileService.uploadFile(file.getOriginalFilename(), file.getBytes());
						
						// 6-14. boardFile 정보 저장 
						if(boardFile.getBoardId() == null) { // 6-15. boardFile 엔티티의 id가 null이라면 파일을 아직 등록하지 않은 회원이니 
							
							// 6-16. 6-8에서 만든 새로운 boardFile에 업로드할 파일 정보를 set 한다음
							boardFile.setFileName(file.getOriginalFilename());
							boardFile.setFileUrl(createdFilename);
							boardFile.setDownCnt(0);
							boardFile.setUserId(loginUser.getId());
							boardFile.setBoardId(findBoard.getId());
							boardFile.setCreatedAt(LocalDateTime.now());
							
							// 6-17. 영속화
							boardFileRepository.save(boardFile);
							
						} else { // 6-18. 6-15가 아니라면 기존 boardFile 정보 변경(변경 감지)
							boardFile.updateBoardFile(file, createdFilename, updatedBoard);	
						}
						
						// 6-19. 6-11에서 가져온 boardEntity에 만들어진 파일 정보 저장(더티 체킹)
						updatedBoard.imageUpdate(file.getOriginalFilename(), createdFilename);
				
					} catch (MaxUploadSizeExceededException e) {
						throw new CustomApiException(e.getMessage());
					} catch (SizeLimitExceededException e) {
						throw new CustomApiException(e.getMessage());
					}
					
				}
				
				return new BoardInfoRespDto(updatedBoard, boardFile);
				
			} else {
				// 6-20. 업로드할 이미지 파일이 없다면 찾아온 board에 변경된 board 데이터만 저장(기존 이미지는 있을 수도 있음)
				updatedBoard = findBoard.updateBoard(board);
				
				// 6-21. 업로드 할 파일이 없으니 기존에 있던 boardFile을 반환하거나 아니면 빈껍데기 boardFile을 반환
				return new BoardInfoRespDto(updatedBoard, boardFile);
			}
			
		} else {
			throw new CustomApiException("글을 수정할 수 있는 권한이 있는 유저가 아닙니다.");
		}
	}
	
	public BoardPagingRespDto findAllByPaging(final CommonParams params) {
		Map<String, Object> result = new HashMap<>();
		
		// 7-2. 게시글 갯수 조회
		int count = boardMapper.count(params);
		
		// 7-3. 페이지네이션 정보 계산 후 CommonParams에 셋팅
		Pagination pagination = new Pagination(count, params);
		params.setPagination(pagination);

		// 7-4. 등록된 게시글이 없는 경우, 페이징 데이터 1로 초기화해 세팅 후 비어있는 list를 보내준다.
		if(count < 1) {
			List<BoardInfoRespDto> list = new ArrayList<>();
			
			pagination = new Pagination(1, params);
			params.setPagination(pagination);
			
			result.put("params", params);
			result.put("list", list);
			
			return new BoardPagingRespDto(result);
		} 
			// 7-5. 게시글 리스트 조회
		List<BoardInfoRespDto> list = boardMapper.findAll(params);
			
		// 7-6. 데이터 반환
		result.put("params", params);
		result.put("list", list);
		
		return new BoardPagingRespDto(result);
	}
	
}
