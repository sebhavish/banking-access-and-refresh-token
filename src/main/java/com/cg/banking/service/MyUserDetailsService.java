package com.cg.banking.service;

import com.cg.banking.beans.MyUserDetails;
import com.cg.banking.beans.User;
import com.cg.banking.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String mobileNumber) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findById(mobileNumber);
                user.orElseThrow(()->
                        new UsernameNotFoundException(String.format("user %s is not found",mobileNumber)));

                return user.map(MyUserDetails::new).get();
    }
}
