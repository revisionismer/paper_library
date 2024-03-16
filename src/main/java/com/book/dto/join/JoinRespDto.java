package com.book.dto.join;

import com.book.domain.user.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class JoinRespDto {

	private Long id;
	private String username;
	private String email;
	
	public JoinRespDto(Long id, String username, String email) {
		this.id = id;
		this.username = username;
		this.email = email;
	}
	
	public JoinRespDto(User userEntity) {
		this.id = userEntity.getId();
		this.username = userEntity.getUsername();
		this.email = userEntity.getEmail();
	
	}
}