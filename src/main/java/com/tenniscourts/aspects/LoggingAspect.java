package com.tenniscourts.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("within(com.tenniscourts.guests.GuestService) || within(com.tenniscourts.reservations.ReservationService)" +
            " || within(com.tenniscourts.schedules.ScheduleService) || within(com.tenniscourts.tenniscourts.TennisCourtService))")
    public void serviceLevelPointCuts() {

    }

    @Around("serviceLevelPointCuts()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
                log.info("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), result);
            return result;
        } catch (Exception e) {
            log.error("Exception {} in {} with message = {}",  e.getClass().getName(), joinPoint.getSignature().getDeclaringTypeName(), e.getMessage());
            throw e;
        }
    }
}
