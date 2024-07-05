package com.book.service.user;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.book.domain.user.User;
import com.book.domain.user.UserRepository;
import com.book.dto.join.JoinReqDto;
import com.book.dto.join.JoinRespDto;
import com.book.dto.user.UserReqDto;
import com.book.dto.user.UserRespDto.UserInfoRespDto;
import com.book.dto.user.UserRespDto.UserProfileRespDto;
import com.book.dto.user.UserRespDto.UserUpdateRespDto;
import com.book.handler.exception.CustomApiException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)  // RuntimeException 말고도 모든 예외가 터졌을시 롤백시킨다.
@RequiredArgsConstructor
public class UserService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	@Value("${user.path}")
	private String uploadFolder;
	
	public JoinRespDto join(JoinReqDto joinReqDto) {
		log.info("회원가입");
		// 1-1. 동일 회원 아이디가 있는지 확인
		Optional<User> userOp = userRepository.findByUsername(joinReqDto.getUsername());
		
		if(userOp.isPresent()) {
			throw new CustomApiException("중복된 아이디입니다.");
		}
		
		// 1-5. 웹화면에서 요청이 아닌(postman으로 호출) 경우 서버에서도 비밀번호 검증
		if(!joinReqDto.getPassword().equals(joinReqDto.getPassword_chk())) {
			throw new CustomApiException("비밀번호가 동일하지 않습니다.");
		}
		
		// 1-2. 패스워드 인코딩 + 회원가입
		User userPS = userRepository.save(joinReqDto.toEntity(passwordEncoder));
		
		// 1-4. dto 응답
		return new JoinRespDto(userPS);
	}
	
	@Transactional(readOnly = true)
	public UserInfoRespDto readUserInfo(User loginUser) {
		// 2-1. 전달 받은 User entity 정보가 있는지 확인
		Optional<User> userOp = userRepository.findByUsername(loginUser.getUsername());
						
		if(userOp.isPresent()) {
			User findUser = userOp.get();
							
			return new UserInfoRespDto(findUser);
							
		} else {
			throw new CustomApiException("해당 유저가 존재하지 않습니다.");
		}
		
	}
	
	public UserProfileRespDto userProfileImgUpdate(User loginUser, MultipartHttpServletRequest request) {
		
		User userEntity = userRepository.findById(loginUser.getId()).orElseThrow(() -> {
			throw new CustomApiException("해당 유저를 찾을 수 없습니다.");
		});
		
		List<MultipartFile> files = request.getFiles("files");
	
		// 3-1. 파일명 + 확장자 가져오기
		String originalFileName = files.get(0).getOriginalFilename();
						
		// 3-2. 확장자 추출
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
						
		// 3-3. UUID[Universally Unique IDentifier] : 네트워크 상에서 고유성이 보장되는 id를 만들기 위한 표준 규약. 범용고유식별자.
		UUID uuid = UUID.randomUUID();
						
		// 3-4. UUID + 확장자 명으로 저장
		String imageFileName = uuid.toString() + extension;
				
		// 3-5. 3-4에서 가져온 실제 파일이  저장될 경로에 3-3에서 만든 UUID 파일명을 더해서 Path를 만든다.
		Path imageFilePath = Paths.get(uploadFolder + imageFileName);
				
		System.out.println(imageFilePath);
				
		// 3-6. 파일을 실제 저장 경로에 저장하는 로직 -> 통신 or I/O -> 예외가 발생할 수 있다(try-catch로 묶어서 처리)
		try {
					
			Files.write(imageFilePath, files.get(0).getBytes());
					
		} catch (Exception e) {
			throw new MaxUploadSizeExceededException(1000);
		}
		
		userEntity.setProfileImageUrl(imageFileName);
		userEntity.setUpdatedAt(LocalDateTime.now());
		
		return new UserProfileRespDto(loginUser);
				
	}
	
	public UserUpdateRespDto userUpdate(User loginUser, UserReqDto userReqDto) {
		// 4-1. 로그인 유저 정보를 가져온다.(영속 상태, 컨트롤러에서 전달받은 User 객체는 영속상태가 아님.)
		User userEntity = userRepository.findById(loginUser.getId()).orElseThrow(() -> {
			throw new CustomApiException("해당 유저를 찾을 수 없습니다.");
		});
		// 4-2. 회원 정보 변경 화면에서 가져온 계정의 비밀번호를 가져온다.
		String rawPassword = userReqDto.getPassword();
		
		// 4-3. 회원 정보 변경 화면에서 가져온 계정의 변경할 비밀번호를 가져온다.
		String changedPassword = userReqDto.getConvertPassword();
		
		System.out.println(passwordEncoder.matches(rawPassword, loginUser.getPassword()));
		
		// 4-4. passwordEncoder의 matches 메소드는 인코딩된 비밀번호가 매개변수로 들어온 문자열과 일치하는지 내부적으로 확인해주고 맞으면 true, 아니면 false를 반환. 
		if(passwordEncoder.matches(rawPassword, loginUser.getPassword())) {
			// 4-5. 4-4를 통과했다면 계정의 원래 비밀번호를 정상적으로 맞게 입력했다는 의미이므로 변경할 비밀번호를 인코딩해서 encodedPassword에 저장한다.
			String encodedPassword = passwordEncoder.encode(changedPassword);
			
			// 4-6. 전달 받은 이메일로 변경(영속화) -> 변경 감지.
			userEntity.setEmail(userReqDto.getEmail());
			
			// 4-7. 비밀번호 변경(영속화) -> 변경 감지.
			userEntity.setPassword(encodedPassword);	
			userEntity.setUpdatedAt(LocalDateTime.now());
			
			System.out.println("바뀐 비번 : " + loginUser.getPassword());
			System.out.println("인코딩 비번 : " + encodedPassword);
			
			return new UserUpdateRespDto(userEntity);
						
		} else {
			throw new CustomApiException("현재 비밀번호가 일치하지 않습니다.");
		}
		
	}
	
	

}