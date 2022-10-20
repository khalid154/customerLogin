package com.customer.login.repository;

import com.customer.login.modal.CustomerOTPContact;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPContactRepository extends PagingAndSortingRepository<CustomerOTPContact, Long> {

    CustomerOTPContact findByPhoneNumber(String contactNo);

}
