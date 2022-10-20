package com.customer.login.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.customer.login.exception.LoginCustomException;
import com.customer.login.request.OTPRequestDTO;
import com.customer.login.request.OTPVerificationRequestDTO;
import com.customer.login.request.UserRequest;
import com.customer.login.response.*;
import com.customer.login.service.ManufacturerService;
import com.customer.login.service.OTPService;
import com.customer.login.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin
@Log4j2
@RequestMapping("/customer")
public class LoginController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Autowired
    private OTPService otpService;
    @Autowired
    private ManufacturerService userService;


    @PostMapping("/requestOtp")
    public RestApiResponse getOtpRequest(@RequestBody OTPRequestDTO rq) {
        OTPResponse response = requestOtp(rq.getContactNo());
        return new RestApiResponse(true, HttpStatus.OK.value(), "otp sent", response.getToken());
    }


    @PostMapping("/verifyOtp")
    public RestApiResponse<Map<String, Object>> authenticateViaOtpForGuestUser(@Valid @RequestBody OTPVerificationRequestDTO enteredOtpRQ) {
        OTPResponse response = otpService.authenticateUserEnteredOtp(enteredOtpRQ);
        log.info("otp status->" + response.isStatus());
        if (!response.isStatus()) {
            throw new LoginCustomException("INVALID_OTP");
        }
        log.info("successfully set contact no. in to cache ->" + enteredOtpRQ.getContactNo());
        UserDetails user = userService.getOrCreateUser(enteredOtpRQ);
        final String token = jwtTokenUtil.generateToken(user);
        Map<String, Object> res = new HashMap<>();
        res.put("token", token);
        res.put("countryCode", enteredOtpRQ.getCountryCode());
        return new RestApiResponse(true, HttpStatus.OK.value(), "", res);
    }


    @PostMapping(value = "/authenticateViaOtp")
    public RestApiResponse authenticateViaOtp(@RequestBody UserRequest authenticationRequest) throws LoginCustomException {
        final UserDetails userDetails = userService.getUser(authenticationRequest.getUsername());
        if (userDetails == null) {
            throw new LoginCustomException("CONTACT_NO_NOT_REGISTERED");
        }
        OTPVerificationRequestDTO otpVerificationRQModel = new OTPVerificationRequestDTO();
        otpVerificationRQModel.setContactNo(authenticationRequest.getContactNo());
        otpVerificationRQModel.setOtp(authenticationRequest.getOtp());
        boolean otpVerified = false;
        if (!otpVerified) {
            throw new LoginCustomException("INVALID_OTP");
        }
        final String token = jwtTokenUtil.generateToken(userDetails);
        return new RestApiResponse(true, 200, null, new JwtResponse(token));
    }

    private OTPResponse requestOtp(String phoneNumber) {
        OTPRequestDTO otpRequest = new OTPRequestDTO();
        otpRequest.setContactNo(phoneNumber);
        return otpService.generateOtpForLogin(otpRequest);
    }
}
