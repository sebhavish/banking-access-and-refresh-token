package com.cg.banking.service;

import com.cg.banking.beans.AccountType;
import com.cg.banking.beans.BankAccount;
import com.cg.banking.beans.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.cg.banking.beans.Roles.ROLE_ADMIN;
import static com.cg.banking.beans.Roles.ROLE_USER;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BankAccountServiceTest {
    @Autowired
    private IBankAccountService bankAccountService;
    @Autowired
    private UserService userService;

    @Test
    void getAccount() {
        User user = new User("9951830121","Bhavish","password",null,ROLE_ADMIN);
        User returnedUser = userService.addUser(user);
        BankAccount bankAccount = new BankAccount("INDB0000546",1000, AccountType.SAVINGS,user);
        BankAccount returnedBankAccount = bankAccountService.addAccount(bankAccount);
        assertEquals(bankAccount.getAccountNo(),bankAccountService.getAccount(returnedBankAccount.getAccountNo()).getAccountNo());
        assertNull(bankAccountService.getAccount(523L));
        assertThrows(NullPointerException.class, (Executable) bankAccountService.getAccount(35L));
    }

    @Test
    void getAllAccounts() {
        assertNotNull(bankAccountService.getAllAccounts());
    }

    @Test
    void addAccount() {
        User user = new User("9868450258","Eswar","password",null,ROLE_USER);
        User returnedUser = userService.addUser(user);
        BankAccount bankAccount = new BankAccount("INDB0006542",10000,AccountType.CURRENT,returnedUser);
        BankAccount returnedBankAccount = bankAccountService.addAccount(bankAccount);
        assertEquals(bankAccount.getAccountNo(),returnedBankAccount.getAccountNo());
        assertThrows(Exception.class,()->bankAccountService.addAccount(new BankAccount("IND009",1000,AccountType.SAVINGS,returnedUser)));
    }

    @Test
    void updateAccount() {
        User user = new User("9984782565","Tarun","password",null,ROLE_USER);
        User returnedUser = userService.addUser(user);
        BankAccount bankAccount = new BankAccount("INDB0006784",1500,AccountType.LOAN,user);
        BankAccount returnedBankAccount = bankAccountService.addAccount(bankAccount);
        Long accountNumber = returnedBankAccount.getAccountNo();
        BankAccount updatedBankAccount = new BankAccount("INDB0006784",2000,AccountType.LOAN,user);
        updatedBankAccount.setAccountNo(accountNumber);
        assertEquals(2000,bankAccountService.updateAccount(updatedBankAccount).getBalance());
    }

    @Test
    void deleteAccount() {
        User user = new User("9876543210","Alekhya","password",null,ROLE_USER);
        User returnedUser = userService.addUser(user);
        BankAccount bankAccount = new BankAccount("INDB0006784",1000,AccountType.SAVINGS,user);
        BankAccount returnedBankAccount = bankAccountService.addAccount(bankAccount);
        Long accountNumber = returnedBankAccount.getAccountNo();
        bankAccountService.deleteAccount(accountNumber);
        assertNull(bankAccountService.getAccount(accountNumber));
    }
}