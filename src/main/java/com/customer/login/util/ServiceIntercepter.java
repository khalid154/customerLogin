package com.customer.login.util;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class ServiceIntercepter implements HandlerInterceptor {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public boolean preHandle(
			HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		logger.info("{}","Intercepter invoked");
		String transactionId=request.getHeader(KbRestTemplate.TRANSACTION_ID);
		String authrizationCode=request.getHeader(KbRestTemplate.AUTHORIZATION);
		if(transactionId==null) {
			transactionId=UUID.randomUUID().toString();
		}
		RequestDataKeeper.set(KbRestTemplate.TRANSACTION_ID, transactionId);
		RequestDataKeeper.set(KbRestTemplate.AUTHORIZATION, authrizationCode);
		logger.info("transactionid {}",transactionId);
		return true;
	}
	@Override
	public void postHandle(
			HttpServletRequest request, HttpServletResponse response, Object handler, 
			ModelAndView modelAndView) throws Exception {
		RequestDataKeeper.remove();
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
			Object handler, Exception exception) throws Exception {}
}