package com.customer.login.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OTPResponse {
    private String token;
    private boolean status;
    private String errorMsg;

}

