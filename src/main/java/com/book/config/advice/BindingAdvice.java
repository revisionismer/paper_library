package com.book.config.advice;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

// import org.aspectj.lang.annotation.Before;
// import org.aspectj.lang.annotation.After;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.book.handler.exception.CustomValidationException;

@Aspect
@Component
public class BindingAdvice {

//	@Before("execution(* com.tistory.web.*Controller.*(..))")
	public void before() {
		System.out.println("AOP 실험 시작");
	}
	
//	@After("execution(* com.tistory.web.*Controller.*(..))")
	public void after() {
		System.out.println("AOP 실험 종료");
	}
	
	@Around("execution(* com.book.web.*.*ApiController.*(..))")
	public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
		Object[] args = proceedingJoinPoint.getArgs();
		
		for(Object arg : args) {
			if(arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult) arg;
				
				if(bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();
					
					for(FieldError error : bindingResult.getFieldErrors()) {
						
						errorMap.put(error.getField(), error.getDefaultMessage());
						System.out.println(error.getDefaultMessage());
						
					}
					
					throw new CustomValidationException("유효성 검사 실패", errorMap);
				}
			}
		}
		
		return proceedingJoinPoint.proceed();
	}
}
