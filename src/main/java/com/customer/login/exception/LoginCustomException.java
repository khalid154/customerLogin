package com.customer.login.exception;

public class LoginCustomException extends RuntimeException{

    public LoginCustomException(String message) {
        super(message);
    }

    public LoginCustomException(Throwable cause) {
        super(cause);
    }
}
