package com.multi.erp.aop;

import org.apache.ibatis.transaction.Transaction;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

//@Component
//@Aspect
public class TxAdvice {
	@Autowired
	private PlatformTransactionManager transactionManager;
	@Pointcut("execution(* com.multi.erp.*.*Service*.insert(..))")
	public void inserTx() {
		
	}
	@Around("inserTx()")
	public Object applyTx(ProceedingJoinPoint joinPoint) throws Throwable {
		TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		try {
			Object object = joinPoint.proceed();
			transactionManager.commit(transactionStatus);
			return object;
		}catch (RuntimeException e) {
			// 오류상황
			transactionManager.rollback(transactionStatus);
			throw e;
		}
	}
}
