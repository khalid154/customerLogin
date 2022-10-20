package com.customer.login.modal;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity(name="Customer_Otp_Contact")
@Data
@NoArgsConstructor
public class CustomerOTPContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @NotNull
    @Column(name = "Phone_Number")
    private String phoneNumber;

    @NotNull
    @Column(name = "Otp")
    private String otp;

}
