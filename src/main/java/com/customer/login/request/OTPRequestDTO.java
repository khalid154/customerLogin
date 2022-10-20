package com.customer.login.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OTPRequestDTO {

    private String userName;
    private String userSessionToken;
    private String otp;
    private String contactNo;

}