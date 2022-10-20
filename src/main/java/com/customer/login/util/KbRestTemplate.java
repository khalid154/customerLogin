package com.customer.login.util;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

@Service
public class KbRestTemplate {
   
	private static final Logger logger = LoggerFactory.getLogger(KbRestTemplate.class);
	static RestTemplate restTemplate =new RestTemplate();
    public static String TRANSACTION_ID="transactionid";
    public static String AUTHORIZATION="Authorization";
	static Map<Integer, RestTemplate> map=new ConcurrentHashMap<>();
	public RestTemplate getRestTemplate(int timeout) {
		if(map.size()>200) {
			String jsonMap=new Gson().toJson(map);
			logger.error("Not creating new resttemplate returning default beause map size is full {}",jsonMap);
			return restTemplate;
		}
		if(map.containsKey(timeout)) {
			return map.get(timeout);
		}
		RestTemplate restTemplate=new RestTemplate();
		addTimeout(restTemplate,timeout);
		map.put(timeout, restTemplate);
		return restTemplate;
	}
	
	public ResponseEntity<Map> exchange(String url,int timeoutoutInMs) {
		return exchange(url,HttpMethod.GET,timeoutoutInMs);
	}
	public ResponseEntity<Map> exchange(String url,HttpMethod httpMethod,int timeoutoutInMs) {
		return exchange(url,httpMethod, getEntity(), timeoutoutInMs);
	}
	public ResponseEntity<Map> exchange(String url,HttpMethod httpMethod,HttpEntity<?> entity,int timeoutoutInMs) {
		return exchange(url,httpMethod, entity,timeoutoutInMs, Map.class);
	}
	public <T> ResponseEntity<T> exchange(String url,HttpMethod httpMethod,HttpEntity<?> entity,int timeoutoutInMs,Class<T> responseClass)  {
		RestTemplate restTemplate=getRestTemplate(timeoutoutInMs);
		long startTime=System.currentTimeMillis();
		ResponseEntity<T> response=null;
		printurl(url);
		try {
			entity=addTransactionIdInHeader(entity);
			response= restTemplate.exchange(url,	httpMethod, entity, responseClass);
			logResponse(response, url, System.currentTimeMillis()-startTime);
		}catch (HttpStatusCodeException e) {
			logException(e);
			throw e;
		}catch(Exception e) {
			logException(e);
		}
		return response;
	}
	private void printurl(String url) {
		logger.info("microservice url {}",url);
	}
	private void logException(Exception e) {
		logger.error("Exception Occure in communication :: ",e);
	}
	private void logResponse(ResponseEntity response, String url, long millisecond) {
		logger.info("microservice url {} responsetime {}",url,millisecond);
		logger.info("Status Code {}",response.getStatusCode());
		logger.debug("Status Code {} body {}",response.getStatusCode(),response.getBody());
	}
	public <T> ResponseEntity<T> postForEntity(String url,HttpEntity<?> entity,int timeoutoutInMs,Class<T> responseClass) {
		RestTemplate restTemplate=getRestTemplate(timeoutoutInMs);
		long startTime=System.currentTimeMillis();
		ResponseEntity<T> response=null;
		try {
			entity=addTransactionIdInHeader(entity);
			printurl(url);
			logger.info("headers :{}",new Gson().toJson(entity.getHeaders().entrySet()));
			logger.info("requestBody :: {}",new Gson().toJson(entity.getBody()));
			response=  restTemplate.postForEntity(url, entity, responseClass);
			logResponse(response, url, System.currentTimeMillis()-startTime);
		}catch (HttpStatusCodeException e) {
			logException(e);
			throw e;
		}catch(Exception e) {
			logException(e);
		}
		return response;
	}
	public <T> ResponseEntity<T> postForEntity(String url,Object postData,int timeoutoutInMs,Class<T> responseClass) {
		return this.postForEntity(url, getEntity(postData), timeoutoutInMs, responseClass);
	}

	public <T> ResponseEntity<T> getForEntity(String url,int timeoutoutInMs,Class<T> responseClass) {
		return this.exchange(url, HttpMethod.GET, getEntity(), timeoutoutInMs, responseClass);
	}
	private  HttpEntity<?> getEntity(){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(TRANSACTION_ID, getTransactionId());
		return new HttpEntity<>(headers);
	}
	private  HttpEntity<?> getEntity(Object postData){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(TRANSACTION_ID, getTransactionId());
		return new HttpEntity<>(postData,headers);
	}

	private HttpEntity addTransactionIdInHeader(HttpEntity entity) {
		HttpHeaders existingHeader =entity.getHeaders();
		HttpHeaders headers=new HttpHeaders();
		for(String key:existingHeader.keySet()){
			headers.add(key, existingHeader.get(key).get(0));
		}
		headers.add(TRANSACTION_ID, getTransactionId());
		headers.add(AUTHORIZATION, getAuthrizationCode());
		return new HttpEntity<>(entity.getBody(),headers);
	}
	private String getTransactionId() {
		Object transactionId=RequestDataKeeper.get(TRANSACTION_ID);
		if(transactionId==null) {
			transactionId=UUID.randomUUID().toString();
			RequestDataKeeper.set(TRANSACTION_ID, transactionId);
		}
		return (String)transactionId;
	}
	private String getAuthrizationCode() {
		String transactionId=(String)RequestDataKeeper.get(AUTHORIZATION);
		return (String)transactionId;
	}
	private void addTimeout(RestTemplate restTemplate, int timeoutoutInMs) {
		if(timeoutoutInMs>0) {
			ClientHttpRequestFactory cl= restTemplate.getRequestFactory();
			if (cl instanceof SimpleClientHttpRequestFactory) {
				SimpleClientHttpRequestFactory req=(SimpleClientHttpRequestFactory)cl;
				req.setConnectTimeout(timeoutoutInMs);
				req.setReadTimeout(timeoutoutInMs);
			} 
		}
	}
}
