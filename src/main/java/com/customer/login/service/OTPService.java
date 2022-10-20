package com.customer.login.service;


import com.customer.login.modal.Customer;
import com.customer.login.repository.CustomerRepository;
import com.customer.login.request.OTPRequestDTO;
import com.customer.login.request.OTPVerificationRequestDTO;
import com.customer.login.response.OTPResponse;
import com.customer.login.util.CacheHandler;
import com.customer.login.util.KonnectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class OTPService {

    @Autowired
    @Qualifier("twoFactorIOtpImpl")
    private IOtp otp;

    @Autowired
    CacheHandler cacheHandler;

    @Autowired
    CustomerRepository customerRepository;


    public OTPResponse generateOtp(OTPRequestDTO otpRQ) {
        log.info("Inside the generateOtp In OtpService");
        Customer userExistsWithContactNo=null;
        try {
            userExistsWithContactNo = customerRepository.findByPhoneNumber(otpRQ.getContactNo());
        }catch (Exception e){
            log.error("",e);
        }

        OTPResponse otpResponse = null;
        if (null != userExistsWithContactNo ) {
            otpResponse = otp.generateOtp(otpRQ);
            log.info("userExistsWithContactNo is not null or profile is not null ");
            log.info("otpResponse.isStatus() "+otpResponse.isStatus() +"->token->"+otpResponse.getToken() );

            if (otpResponse.isStatus() && !KonnectUtils.isNullOrEmpty(otpResponse.getToken())) {
                log.info("Did not set contact and token into cache" );
                cacheHandler.put("OTP" + otpRQ.getContactNo(), otpResponse.getToken());
            }

            if(KonnectUtils.isNullOrEmpty(otpResponse.getToken())) {
                log.info("Token is empty" );
            }

        } else {
            otpResponse = new OTPResponse();
            otpResponse.setStatus(false);
            otpResponse.setErrorMsg("Contact Number Does Not Exists !!");

        }
        return otpResponse;
    }



    public OTPResponse generateOtpForLogin(OTPRequestDTO otpRQ) {
        log.info("Inside the generateOtp In OtpService");
        OTPResponse otpResponse = null;
        otpResponse = otp.generateOtp(otpRQ);
        log.info("otpResponse.isStatus() " + otpResponse.isStatus() + "->token->" + otpResponse.getToken());

        if (otpResponse.isStatus() && !KonnectUtils.isNullOrEmpty(otpResponse.getToken())) {
            log.info("Did not set contact and token into cache");
            System.out.println("token>>" + otpResponse.getToken());
            cacheHandler.put("OTP" + otpRQ.getContactNo(), otpResponse.getToken());
            log.info("cacheHandler token>>" + cacheHandler.get("OTP" + otpRQ.getContactNo()));
        }

        if (KonnectUtils.isNullOrEmpty(otpResponse.getToken())) {
            log.info("Token is empty");
        }

        return otpResponse;
    }

    public OTPResponse authenticateUserEnteredOtp(final OTPVerificationRequestDTO enteredOtpRQ) {
        log.info("Inside the authenticateUserEnteredOtp In OtpService");
        OTPResponse otpResponse = null;
        try {
            enteredOtpRQ.setOtp(enteredOtpRQ.getOtp());
            if(otp.authenticateUserEnteredOtp(enteredOtpRQ)) {
                otpResponse = new OTPResponse();
                otpResponse.setToken("");
                otpResponse.setStatus(true);
                otpResponse.setErrorMsg("OTP VALIDATED");
            }else {
                otpResponse = new OTPResponse();
                otpResponse.setToken("");
                otpResponse.setStatus(false);
                otpResponse.setErrorMsg("NOT A VALID OTP");
            }

        } catch (Exception e) {
            log.error("Exception Inside the authenticateUserEnteredOtp In OtpService",e);
            otpResponse = new OTPResponse();
            otpResponse.setToken("");
            otpResponse.setStatus(false);
            if (e.getMessage().equalsIgnoreCase("400 Bad Request")) {
                otpResponse.setErrorMsg("NOT A VALID OTP");
            } else {
                otpResponse.setErrorMsg("Something went wrong , Please try again ");
            }

        } finally {
            //cacheHandler.remove("OTP" + enteredOtpRQ.getContactNo());
        }
        return otpResponse;
    }

}

