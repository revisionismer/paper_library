package com.book.service.user;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.book.domain.user.User;
import com.book.domain.user.UserRepository;
import com.book.dto.join.JoinReqDto;
import com.book.dto.join.JoinRespDto;
import com.book.handler.exception.CustomApiException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)  // RuntimeException 말고도 모든 예외가 터졌을시 롤백시킨다.
@RequiredArgsConstructor
public class UserService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
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
	

}