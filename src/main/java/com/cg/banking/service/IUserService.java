package com.cg.banking.service;

import com.cg.banking.beans.BankAccount;
import com.cg.banking.beans.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    public Optional<User> getUser(String mobileNumber);
    public List<BankAccount> getUserAccounts(String mobileNumber);
    public List<User> getAllUsers();
    public User addUser(User user);
    public User updateUser(User user);
    public void deleteUser(String mobileNumber);
}
