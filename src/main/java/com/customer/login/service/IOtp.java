package com.customer.login.service;

import com.customer.login.request.OTPRequestDTO;
import com.customer.login.request.OTPVerificationRequestDTO;
import com.customer.login.response.OTPResponse;

public interface IOtp {
    OTPResponse generateOtp(OTPRequestDTO otpRQ);

    boolean authenticateUserEnteredOtp(final OTPVerificationRequestDTO enteredOtpRQ);
}
