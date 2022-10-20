package com.customer.login.exception;
import java.net.ConnectException;
import java.util.List;

import com.customer.login.response.RestApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@Log4j2
@ControllerAdvice
public class ControllerExceptionHandler {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ConnectException.class)
    @ResponseBody
    public RestApiResponse handleConnectException(ConnectException ex) {
        logException(ex);
        RestApiResponse response=new RestApiResponse();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return response;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public RestApiResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        logException(ex);
        RestApiResponse response=new RestApiResponse();
        response.setSuccess(false);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(ex.getMessage());
        return response;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RestApiResponse handleException(Exception ex) {
        logException(ex);
        RestApiResponse response=new RestApiResponse();
        response.setSuccess(false);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage(ex.getMessage());
        return response;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(LoginCustomException.class)
    public RestApiResponse handleException(LoginCustomException ex) {
        logException(ex);
        System.out.println("******************************************");
        RestApiResponse response=new RestApiResponse();
        response.setSuccess(false);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setMessage("Invalid UserName/password");
        return response;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestApiResponse handleInvalidParamException(MethodArgumentNotValidException ex) {
        logException(ex);
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors);
    }
    private RestApiResponse processFieldErrors(List<FieldError> fieldErrors) {
        RestApiResponse restApiResponse=new RestApiResponse();
        restApiResponse.setSuccess(false);
        restApiResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        for (FieldError fieldError: fieldErrors) {
            restApiResponse.setMessage(fieldError.getDefaultMessage());
        }
        return restApiResponse;
    }

    private void logException(Exception e) {
        log.error("Exception came : ",e);
    }
}
