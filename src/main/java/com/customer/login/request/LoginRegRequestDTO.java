package com.customer.login.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
@Builder
public class LoginRegRequestDTO  {

    @Pattern(regexp = "^(?=.{1,50}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$")
    @NotNull
    @Size(min = 1, max = 50)
    private String userName;

    @NotNull
    @Size(min = 1, max = 10)
    private String phoneNumber;
    private Boolean rememberMe;

}
