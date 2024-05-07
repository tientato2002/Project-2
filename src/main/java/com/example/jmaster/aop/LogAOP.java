package com.example.jmaster.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LogAOP {
	
	@Before("execution(* com.example.jmaster.service.DepartmentService.getById(*))")
	public void getByDepartmentId(JoinPoint joinPoint) {
		int id = (Integer)joinPoint.getArgs()[0];
		log.info("Join Point" + id);
	}
}
