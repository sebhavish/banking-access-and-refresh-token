package com.cg.banking.controllers;

import com.cg.banking.beans.BankAccount;
import com.cg.banking.beans.User;
import com.cg.banking.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/{mobileNumber}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public Optional<User> getUser(@PathVariable String mobileNumber, Authentication auth) {
        logger.info("Getting requested user from database");
        if(isRole("ROLE_USER")){
            mobileNumber = auth.getName();
            return userService.getUser(mobileNumber);
        }
        return userService.getUser(mobileNumber);
    }

    @GetMapping("/user/accounts/{mobileNumber}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public List<BankAccount> userAccounts(@PathVariable String mobileNumber) {
        logger.info("Getting user's bank accounts available");
        return userService.getUserAccounts(mobileNumber);
    }

    @GetMapping("/admin/viewAllUsers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> allUsers() {
        logger.info("Getting all users available from the database");
        return userService.getAllUsers();
    }

    @PostMapping("/admin/addUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User addUser(@RequestBody User user) {
        logger.info("Creating new user");
        return userService.addUser(user);
    }

    @PutMapping("/user/updateUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public User updateUser(@RequestBody User user) {
        logger.info("Updating user details as requested");
        return userService.updateUser(user);
    }

    @DeleteMapping("/admin/{mobileNumber}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUser(@PathVariable String mobileNumber) {
        logger.info("Deleting the user");
        userService.deleteUser(mobileNumber);
    }

    public boolean isRole(String role) {
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        boolean isRole = false;
        for (GrantedAuthority authority:authorities){
            isRole = authority.getAuthority().equals(role);
            if(isRole){
                break;
            }
        }
        return isRole;
    }
}
