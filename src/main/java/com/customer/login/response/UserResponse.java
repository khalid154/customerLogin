package com.customer.login.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {

    private String phoneNumber;

    private String profileLink;

    private String fullName;

    private Long manufactureId;

}
