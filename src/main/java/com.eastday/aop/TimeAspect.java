package com.eastday.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 时间记录切面，收集接口的运行时间
 */
@Aspect
@Component
public class TimeAspect {

    // 修正Timer注解的全局唯一限定符
    @Pointcut("@annotation(com.eastday.aop.Timer)")
    private void pointcut(){}

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        //获取目标Logger
        Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        //获取目标类名
        String clazzName = joinPoint.getTarget().getClass().getName();
        //获取目标类方法名称
        String methodName = joinPoint.getSignature().getName();

        long start = System.currentTimeMillis();
        //调用目标方法
        Object result = joinPoint.proceed();
        //打印执行时间
        long time = System.currentTimeMillis() - start;
        log.info("{}: {}: : end ... cost time: {}ms", clazzName, methodName, time);

        return result;
    }


}

