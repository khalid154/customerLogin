package com.customer.login.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PhoneNumerVerificationResponse {
    public Boolean isNewUser;
    public String msg;
    public String token;

}
