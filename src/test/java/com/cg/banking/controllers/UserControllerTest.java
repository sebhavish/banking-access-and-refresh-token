package com.cg.banking.controllers;

import com.cg.banking.beans.AccountType;
import com.cg.banking.beans.BankAccount;
import com.cg.banking.beans.Roles;
import com.cg.banking.beans.User;
import com.cg.banking.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "com.cg.banking")
@AutoConfigureMockMvc
@ContextConfiguration
@SpringBootTest(classes = {UserControllerTest.class})
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Mock
    UserService userService;
    @InjectMocks
    UserController userController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @Order(1)
    public void userAccounts() throws Exception {
        User user = new User("9988776655","bhavish","password",null,Roles.ROLE_USER);
        BankAccount bankAccount1 = new BankAccount("IFSC0008765",4000, AccountType.CURRENT,user);
        bankAccount1.setAccountNo(123L);
        BankAccount bankAccount2 = new BankAccount("IFSC0004564",5000,AccountType.SAVINGS,user);
        bankAccount2.setAccountNo(124L);
        List<BankAccount> bankAccounts = new ArrayList<>();
        bankAccounts.add(bankAccount1);
        bankAccounts.add(bankAccount2);

        when(userService.getUserAccounts("9988776655")).thenReturn(bankAccounts);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/users/user/accounts/{mobileNumber}","9988776655"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].accountNo").isNotEmpty())
                .andDo(print());
        verify(userService).getUserAccounts("9988776655");
    }

    @Test
    @Order(2)
    public void allUsers() throws Exception {
        User user1 = new User("9988776655","bhavish","password",null,Roles.ROLE_USER);
        User user2 = new User("9876543210","eswar","pass",null,Roles.ROLE_ADMIN);
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        when(userService.getAllUsers()).thenReturn(users);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/users/admin/viewAllUsers"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].mobileNumber").isNotEmpty())
                .andDo(print());
        verify(userService).getAllUsers();
    }

    @Test
    @Order(3)
    public void addUser() throws Exception {
        User user = new User("9988776655","bhavish","password",null,Roles.ROLE_USER);

        when(userService.addUser(user)).thenReturn(user);

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(user);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/users/admin/addUser")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mobileNumber").value("9988776655"))
                .andDo(print());
        verify(userService).addUser(user);
    }

    @Test
    @Order(4)
    public void updateUser() throws Exception {
        User user = new User("9988776655","bhavish","password",null,Roles.ROLE_USER);

        when(userService.updateUser(user)).thenReturn(user);

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(user);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/users/user/updateUser")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mobileNumber").value("9988776655"))
                .andDo(print());
        verify(userService).updateUser(user);
    }

    @Test
    @Order(5)
    public void deleteUser() throws Exception {
        User user = new User("9988776655","bhavish","password",null,Roles.ROLE_USER);

        doNothing().when(userService).deleteUser("9988776655");

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/admin/{mobileNumber}","9988776655"))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
        verify(userService).deleteUser("9988776655");
    }
}
