package com.book.web.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.book.config.auth.PrincipalDetails;
import com.book.config.jwt.service.JwtService;
import com.book.domain.user.User;
import com.book.dto.ResponseDto;
import com.book.dto.join.JoinReqDto;
import com.book.dto.join.JoinRespDto;
import com.book.dto.user.UserReqDto;
import com.book.dto.user.UserRespDto.UserInfoRespDto;
import com.book.dto.user.UserRespDto.UserProfileRespDto;
import com.book.dto.user.UserRespDto.UserUpdateRespDto;
import com.book.service.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class UserApiController {

	private final UserService userService;
	private final JwtService jwtService;
	
	@PostMapping("/join")
	public ResponseEntity<?> join(@RequestBody @Valid JoinReqDto joinReqDto, BindingResult bindingResult) {
			
		JoinRespDto joinRespDto = userService.join(joinReqDto);
			
		return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", joinRespDto), HttpStatus.CREATED);
	}
	
	@GetMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		
		jwtService.logout(request, response);
		
		return new ResponseEntity<>(new ResponseDto<>(1, "로그아웃 성공", null), HttpStatus.OK);
	}
	
	@GetMapping("/s/info")
	public ResponseEntity<?> userInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User loginUser = principalDetails.getUser();
		
		UserInfoRespDto userInfoRespDto = userService.readUserInfo(loginUser);
		
		return new ResponseEntity<>(new ResponseDto<>(1, "로그인 유저 정보 조회 성공", userInfoRespDto), HttpStatus.OK);
	}
	
	// 2024-07-02 : 여기까지
	@PutMapping("/s/update/profileImage")
	public ResponseEntity<?> profileImageUpdate(MultipartHttpServletRequest request, @AuthenticationPrincipal PrincipalDetails principalDetails) { // 1-1. html input file의 name 값과 매핑해줘야 한다.(중요)
		
		User loginUser = principalDetails.getUser();
		
		UserProfileRespDto userProfileRespDto = userService.userProfileImgUpdate(loginUser, request);

		return new ResponseEntity<>(new ResponseDto<>(1, "프로필 사진 변경 성공", userProfileRespDto), HttpStatus.OK);
	}
	
	// 2024-07-03 : 여기까지
	@PutMapping("/s/update")
	public ResponseEntity<?> updateUserInfo(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody @Valid final UserReqDto userReqDto, BindingResult bindingResult) {
	
		User loginUser = principalDetails.getUser();

		UserUpdateRespDto userUpdateRespDto = userService.userUpdate(loginUser, userReqDto);

		return new ResponseEntity<>(new ResponseDto<>(1, "회원 정보 수정 성공", userUpdateRespDto), HttpStatus.ACCEPTED);	
		
	}
	
	
}
