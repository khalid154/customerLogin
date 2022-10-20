package com.customer.login.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    private String username;
    private String contactNo;
    private String otp;
    public UserRequest(String username, String password) {
        this.setUsername(username);
    }
    @Override
    public String toString() {
        return "UserRequest [username=" + username  + ", contactNo=" + contactNo + ", otp="
                + otp + "]";
    }
}
