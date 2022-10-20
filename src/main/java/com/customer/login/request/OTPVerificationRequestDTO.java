package com.customer.login.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OTPVerificationRequestDTO {

    private String manufacturerId;

    @NotNull
    @NotBlank
    @NotEmpty
    private String username;

    private String otp;

    @NotNull
    @NotBlank
    @NotEmpty
    private String contactNo;

    @NotNull
    @NotBlank
    @NotEmpty
    private String countryCode;

    @NotNull
    @NotBlank
    @NotEmpty
    private String verficationCode;
}
