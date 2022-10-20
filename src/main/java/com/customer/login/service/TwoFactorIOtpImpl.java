package com.customer.login.service;

import com.customer.login.request.OTPRequestDTO;
import com.customer.login.request.OTPVerificationRequestDTO;
import com.customer.login.response.OTPResponse;
import com.customer.login.util.CacheHandler;
import com.customer.login.util.KbRestTemplate;
import com.customer.login.util.KonnectUtils;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;

@Component("twoFactorIOtpImpl")
@Log4j2
public class TwoFactorIOtpImpl implements IOtp {
    final String apiKey = "ae52ed23-503e-11ed-9c12-0200cd936042";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CacheHandler cacheHandler;

    @Override
    public OTPResponse generateOtp(OTPRequestDTO otpRQ) {
        log.info("Inside the generateOtp with TwoFactorimpl");
        //Sample Contact No : +919560017118
        String url =
                "https://2factor.in/API/V1/" + apiKey + "/SMS/" + otpRQ.getContactNo() + "/AUTOGEN3/OTP_FOS";
        log.info("send OTP URL-------- " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        OTPResponse otpResponse = new OTPResponse();
        Map<String, Object> response = null;
        try {
            response = (Map<String, Object>) restTemplate.exchange(url, HttpMethod.GET, entity, Map.class).getBody();
        } catch (Throwable e) {
            log.error("Throwable Inside the generateOtp with TwoFactorimpl", e);
            e.printStackTrace();
            otpResponse.setErrorMsg("Error _Otp Genration Failed");
        }
        if (!KonnectUtils.isNullOrEmpty(response)) {
            if (response.containsKey("Status")
                    && "Success".equalsIgnoreCase(response.get("Status").toString())
                    && response.containsKey("Details")) {
                otpResponse.setToken(response.get("Details").toString());
                otpResponse.setStatus(true);
            }
        }
        return otpResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean authenticateUserEnteredOtp(OTPVerificationRequestDTO enteredOtpRQ) {
        log.info("Inside the authenticateUserEnteredOtp of TwoFactorimpl");

        String url = "https://2factor.in/API/V1/" + apiKey + "/SMS/VERIFY/" + enteredOtpRQ.getVerficationCode() + "/" + enteredOtpRQ
                .getOtp();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        Map<String, Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class).getBody();

        if (!KonnectUtils.isNullOrEmpty(response)) {
            if (response.containsKey("Status") && "Success"
                    .equalsIgnoreCase(response.get("Status").toString())) {
                return true;
            }
        }
        return false;
    }
}
