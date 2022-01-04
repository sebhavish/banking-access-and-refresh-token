package com.cg.banking.service;

import com.cg.banking.beans.BankAccount;
import com.cg.banking.beans.User;
import com.cg.banking.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> getUser(String mobileNumber) {

        return userRepository.findById(mobileNumber);
    }

    @Override
    public List<BankAccount> getUserAccounts(String mobileNumber) {
        return userRepository.findBankAccounts(mobileNumber);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User updateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String mobileNumber) {
        userRepository.deleteById(mobileNumber);
    }
}
