package com.friends.management.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditLogAspect {
    @After("@annotation(auditLog)")
    public void logAction(JoinPoint joinPoint, AuditLog auditLog) {
        String action = auditLog.action();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        System.out.println("Action: " + action);
        System.out.println("Method: " + methodName);
        System.out.println("Class: " + className);
    }
}
