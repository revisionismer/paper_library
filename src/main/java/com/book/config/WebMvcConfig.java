package com.book.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {  // 1-1. 

	@Value("${board.path}")
	private String boardFolder;
	
	@Value("${user.path}")
	private String userFolder;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	
		registry
				.addResourceHandler("/thumnail/**")  // 1-7. /thumnail/** 패턴으로 요청이 오면
				.addResourceLocations("file:///" + boardFolder)  // 1-8. 1-7 경로로 오면 application.properties에 등록된 thumnail.path 경로로 바꿔준다.
				.setCachePeriod(60*10*6)   // 1-9. 60초 * 10 = 600초 = 10분, * 6을 하면 60분 = 1시간 
				.resourceChain(true)  // 1-10. true = resourceChain 발동
				.addResolver(new PathResourceResolver()); // 1-11. 등록
		
		registry
				.addResourceHandler("/userImg/**")
				.addResourceLocations("file:///" + userFolder)
				.setCachePeriod(60*10*6)   
				.resourceChain(true)  
				.addResolver(new PathResourceResolver()); 
	}
}
