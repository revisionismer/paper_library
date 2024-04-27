package com.book.domain.user;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.book.constant.user.UserEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // 1-3. JPA에서 스프링이 User 객체생성할 때 빈생성자로 new를 하기 때문에 추가(중요)
@Getter @Setter
@Table(name = "user_tb")
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;  // 1-1. PK
	
    @Column(length = 20, nullable = false, unique = true)
    private String username;

    // 1234 -> SHA256(해시 알고리즘) -> AB4539GDUF3AE -> 이렇게 안하면 시큐리티 거부
    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 60, nullable = false)
    private String email;

    @Column(nullable = true)
    private String profileImageUrl;  // 1-10. 프로필 이미지 경로
    
    @Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserEnum role; // ADMIN, CUSTOMER  // 1-9. 유저 권한
	
 // 1-14. JPA LocalDateTime 자동 생성 방법 3 : LocalDateTime에 @CreatedDate, @LastModifiedDate 어노테이션을 걸어준다.
 	@CreatedDate
 	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
 	@Column(nullable = false)
 	private LocalDateTime createdAt;  // 1-10. 생성일
 	 
 	@LastModifiedDate
 	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
 	@Column(nullable = true)
 	private LocalDateTime updatedAt;  // 1-11. 수정일
 	
 	private String refreshToken;
	
	// 1-15. User entity builder 만들기
	@Builder
	public User(Long id, String username, String password, String email, String fullname, UserEnum role, LocalDateTime createdAt, LocalDateTime updateAt) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
		this.createdAt = createdAt;
		this.updatedAt = updateAt;
	}
	
    
}
