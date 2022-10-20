package com.customer.login.service;

import com.customer.login.modal.Customer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Log4j2
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private ManufacturerService userService;



    public Customer getUser(String userName) {
        return userService.getUser(userName);
    }
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        log.info("Inside the loadUserByUsername with username {}",userName);
        long t1= System.currentTimeMillis();
        try {
            long s1=System.currentTimeMillis();
            Customer user=getUser(userName);
            long s2=System.currentTimeMillis();
            log.info("Time taken to fetch profile from DB ->"+(s2-s1));
            if(user!=null) {
                org.springframework.security.core.userdetails.User userFDetail=	new org.springframework.security.core.userdetails.User(user.getId()+"", user.getPassword(), new ArrayList<>());
                long t2 =System.currentTimeMillis();
                log.info("Time taken in active profile --> {}",(t2 -t1));
                return userFDetail;
            }else {
                throw new UsernameNotFoundException("User inActive with userId: " + userName);
            }
        } catch (Exception e) {
            log.error("exception occured in find user ::: ",e);
            throw new UsernameNotFoundException("User not found with userId: " + userName);
        }
    }
    public boolean ifUserExists(String username) {
        log.info("Inside the ifUserExists with username {}",username);
        Customer user = getUser(username);
        return user!=null;
    }



}