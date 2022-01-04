package com.cg.banking.controllers;

import com.cg.banking.beans.BankAccount;
import com.cg.banking.service.BankAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private BankAccountService bankAccountService;
    Logger logger = LoggerFactory.getLogger(AccountController.class);

    @GetMapping("/user/{accountNumber}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public BankAccount getAccount(@PathVariable Long accountNumber) {
        logger.info("Getting the requested account");
        return bankAccountService.getAccount(accountNumber);
    }

    @GetMapping("/admin/getAllAccounts")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<BankAccount> getAllAccounts() {
        logger.info("fetching all the bank accounts available");
        return bankAccountService.getAllAccounts();
    }

    @PostMapping("/admin/addAccount")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BankAccount addAccount(@RequestBody BankAccount bankAccount) {
        logger.info("Creating the bank account");
        return bankAccountService.addAccount(bankAccount);
    }

    @PutMapping("/admin/updateAccount")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BankAccount updateAccount(@RequestBody BankAccount bankAccount) {
        logger.info("Updating the bank account details");
        return bankAccountService.updateAccount(bankAccount);
    }

    @DeleteMapping("/admin/{accountNumber}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteAccount(@PathVariable Long accountNumber) {
        logger.info("Deleting the requested account");
        bankAccountService.deleteAccount(accountNumber);
    }
}
