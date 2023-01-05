package com.multi.erp.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
//공통관심사항을 정의할 클래스 -advice
@Component
//aop를 적용할 advice임을 나타내는 어노테이션기호(aop로 서비스할 클래스 - 콩통모듈이라는 의미)
@Aspect
public class TimeCheckAopObj {
	//공통로직이 어떤 핵심로직에서 실행될 것인지 정의, 어느 시점에서 실행될 것인지 정의
	//@Around는 메소드 실행전 메소드 실행 후에 모두 호출
	//board패키지의 모든 클래스의 모든 메소드에 적용하겠다는 의미
	@Around("execution(* com.multi.erp.board..*Service*.*(..))")	//모듈화 	
	public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		Object object = null;
		try {
			System.out.println("start:::::::::aop시작"+joinPoint.toString());
			object = joinPoint.proceed();
			return object;
		} finally {
			long end = System.currentTimeMillis();
			System.out.println("End::::::::::::"+joinPoint.toString()+"===="+(end-start));
		}
	}
}
