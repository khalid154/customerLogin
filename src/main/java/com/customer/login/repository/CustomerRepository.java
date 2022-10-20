package com.customer.login.repository;

import com.customer.login.modal.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {
    Customer findByUserName(String username);

    Customer findByPhoneNumber(String contactNo);

    List<Customer> findByPhoneNumberContaining(String contactNo);

    List<Customer> findUserByPhoneNumber(String contactNo);

    @Query("SELECT COUNT(id) FROM Customer")
    long countAllUsers();
}
