package com.cg.banking.service;

import com.cg.banking.beans.BankAccount;

import java.util.List;

public interface IBankAccountService {
    public BankAccount getAccount(Long accountNumber);
    public List<BankAccount> getAllAccounts();
    public BankAccount addAccount(BankAccount bankAccount);
    public BankAccount updateAccount(BankAccount bankAccount);
    public void deleteAccount(Long accountNumber);
}
