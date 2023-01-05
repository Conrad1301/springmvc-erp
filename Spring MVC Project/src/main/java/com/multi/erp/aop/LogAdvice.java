package com.multi.erp.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogAdvice {
	//메소드를 pointcut로 활용 - 여러 개를 정의할 수 있다.
	@Pointcut("execution(* com.multi.erp.board.*.get*(..))")
	public void mylogpointcut() {}
	
	@Pointcut("execution(* com.multi.erp.board.*.get*(..))")
	public void mylogpointcut2() {}
	
	//위에서 정의한 규칙을 적용(메소드실행 후)
	//@after("mylogpointcut()")
	@After("mylogpointcut()")
	public void log() {
		System.out.println("-----------");
		System.out.println("로그기록");
		System.out.println("-----------");
	}
}
