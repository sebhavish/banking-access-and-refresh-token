package com.cg.banking.service;

import com.cg.banking.beans.BankAccount;
import com.cg.banking.repo.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountService implements IBankAccountService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public BankAccount getAccount(Long accountNumber) {
        return bankAccountRepository.getByAccountNo(accountNumber);
    }

    @Override
    public List<BankAccount> getAllAccounts() {
        return bankAccountRepository.findAll();
    }

    @Override
    public BankAccount addAccount(BankAccount bankAccount) {
        bankAccount.getUser().setPassword(passwordEncoder.encode(bankAccount.getUser().getPassword()));
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public BankAccount updateAccount(BankAccount bankAccount) {
        bankAccount.getUser().setPassword(passwordEncoder.encode(bankAccount.getUser().getPassword()));
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public void deleteAccount(Long accountNumber) {
        BankAccount bankAccount = bankAccountRepository.getByAccountNo(accountNumber);
        bankAccountRepository.delete(bankAccount);
    }
}
