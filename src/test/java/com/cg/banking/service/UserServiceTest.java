package com.cg.banking.service;

import com.cg.banking.beans.AccountType;
import com.cg.banking.beans.BankAccount;
import com.cg.banking.beans.Roles;
import com.cg.banking.beans.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private BankAccountService bankAccountService;

    @Test
    void getUser() {
        User user = new User("9951478986","Rishabh","password",null, Roles.ROLE_USER);
        userService.addUser(user);
        assertEquals(user.getName(),userService.getUser("9951478986").get().getName());
        assertEquals(Optional.empty(), userService.getUser("9865471234"));
    }

    @Test
    void getUserAccounts() {
        User user = new User("9873216545","Bhavish","password",null,Roles.ROLE_ADMIN);
        User returnedUser = userService.addUser(user);
        BankAccount bankAccount = new BankAccount("INDB0005845",1000, AccountType.SAVINGS,user);
       // Long accountNumber = bankAccount.getAccountNo();
        bankAccountService.addAccount(bankAccount);
        assertNotNull(userService.getUserAccounts("9873216545").get(0).getAccountNo());
    }

    @Test
    void addUser() {
        User user = new User("9951456987","Hero","password",null,Roles.ROLE_ADMIN);
        assertEquals(user.getMobileNumber(),userService.addUser(user).getMobileNumber());
    }

    @Test
    void updateUser() {
        User user = new User("9966554422","Tarun","password",null,Roles.ROLE_USER);
        userService.addUser(user);
        user.setName("Bhav");
        assertEquals("Bhav",userService.updateUser(user).getName());
    }

    @Test
    void deleteUser() {
        User user = new User("9966478545","Ram","password",null,Roles.ROLE_USER);
        userService.addUser(user);
        userService.deleteUser("9966478545");
        //assertNull(userService.getUser("9966478545").get());
        assertThrows(NoSuchElementException.class,()->userService.getUser("9966478545").get());
        assertEquals(Optional.empty(),userService.getUser("9966478545"));
    }
    @Test
    void getAllUsers() {
        assertNotNull(userService.getAllUsers());
    }
}