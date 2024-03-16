package com.book.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {  // 1-1. 

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
	}
}
