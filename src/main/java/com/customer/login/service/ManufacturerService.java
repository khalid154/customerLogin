package com.customer.login.service;

import com.customer.login.modal.Customer;
import com.customer.login.request.OTPVerificationRequestDTO;
import com.customer.login.request.UserRequest;
import org.springframework.stereotype.Service;

@Service
public interface ManufacturerService {
    Customer getUser(String userName);
    Customer findUserByContactNo(String contactNo);
    Customer getOrCreateUser(OTPVerificationRequestDTO rq);
    boolean ifUserExists(String userName);
    Customer createUser(UserRequest authenticationRequest);
}
