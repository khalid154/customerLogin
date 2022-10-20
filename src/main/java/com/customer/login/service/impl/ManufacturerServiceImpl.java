package com.customer.login.service.impl;

import com.customer.login.modal.Customer;
import com.customer.login.repository.CustomerRepository;
import com.customer.login.request.OTPVerificationRequestDTO;
import com.customer.login.request.UserRequest;
import com.customer.login.service.ManufacturerService;
import com.customer.login.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class ManufacturerServiceImpl implements ManufacturerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JwtTokenUtil jwtUtil;

    @Override
    public Customer getUser(String userName) {
        Customer user = null;
        if (userName.matches("[0-9]+$")) {
            Optional<Customer> optional = customerRepository.findById(Long.valueOf(userName));
            if (optional.isPresent()) {
                user = optional.get();
            }
            if (user == null) {
                Customer optional2 = customerRepository.findByPhoneNumber((userName));
                if (optional2 != null) {
                    user = optional2;
                }
            }
        } else {
            Customer optional3 = customerRepository.findByUserName((userName));
            if (optional3 != null) {
                user = optional3;
            }
        }
        return user;
    }

    @Override
    public Customer findUserByContactNo(String contactNo) {
        log.info("Inside the findUSerByContactNo  of JwtUserDetailService with contactNo {}", contactNo);
        Customer customer = customerRepository.findByPhoneNumber(contactNo);
        if (customer != null) {
            return customer;
        }
        return null;
    }


    @Override
    public boolean ifUserExists(String userName) {
        return getUser(userName) != null;
    }

    @Override
    public Customer getOrCreateUser(OTPVerificationRequestDTO rq) {
        Customer user = null;
        List<Customer> userList = customerRepository.findUserByPhoneNumber(rq.getContactNo());
        if (userList == null || userList.size() == 0) {
            user = new Customer(rq.getUsername(), rq.getUsername(), rq.getContactNo());
            user.setFullName("");
            user.setPassword("");
            user.setUserName("");
            user.setStatusId(1);
            customerRepository.save(user);
        } else {
            user = userList.get(0);
        }
        return user;
    }

    @Override
    public Customer createUser(UserRequest authenticationRequest) {
        Customer user = new Customer();
        user.setFullName("");
        user.setPhoneNumber(authenticationRequest.getContactNo());
        user.setUserName(authenticationRequest.getUsername());
        user.setPassword("Khalid@123");
        customerRepository.save(user);
        return user;
    }
}
