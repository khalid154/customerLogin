package com.customer.login.response;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestApiResponse<T> {
	private boolean isSuccess;
	private int status;
	private String message;
	private T data;
	private Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
	
	public RestApiResponse(boolean isSucess, int status, String message, T data) {
		super();
		this.isSuccess = isSucess;
		this.status = status;
		this.message = message;
		this.data = data;
	}
	
}
