package com.cg.banking.controllers;

import com.cg.banking.beans.BankAccount;
import com.cg.banking.beans.Roles;
import com.cg.banking.beans.User;
import com.cg.banking.service.BankAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static com.cg.banking.beans.AccountType.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "com.cg.banking")
@AutoConfigureMockMvc
@ContextConfiguration
@SpringBootTest(classes = {AccountControllerTest.class})
public class AccountControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Mock
    BankAccountService bankAccountService;
    @InjectMocks
    AccountController accountController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    @Order(1)
    public void getAccount() throws Exception {
        User user = new User("9876543211","bhavish","password",null, Roles.ROLE_USER);
        BankAccount bankAccount = new BankAccount("ISBN0008789",2000,SAVINGS,user);
        bankAccount.setAccountNo(12345L);

        when(bankAccountService.getAccount(12345L)).thenReturn(bankAccount);

        this.mockMvc.perform(get("/api/accounts/user/{accountNumber}",12345))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNo").value(12345))
                .andDo(print());
        Mockito.verify(bankAccountService).getAccount(12345L);
    }

    @Test
    @Order(2)
    public void getAllAccounts() throws Exception {
        User user = new User("9876543211","bhavish","password",null, Roles.ROLE_USER);
        BankAccount bankAccount1 = new BankAccount("ISBN0007895",5000,CURRENT,user);
        bankAccount1.setAccountNo(12347L);
        BankAccount bankAccount2 = new BankAccount("ISBN0001234",4000,LOAN,null);
        bankAccount2.setAccountNo(12354L);
        List<BankAccount> bankAccounts = new ArrayList<>();
        bankAccounts.add(bankAccount1);
        bankAccounts.add(bankAccount2);

        when(bankAccountService.getAllAccounts()).thenReturn(bankAccounts);
        this.mockMvc.perform(get("/api/accounts/admin/getAllAccounts"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].accountNo").isNotEmpty())
                .andDo(print());
        Mockito.verify(bankAccountService).getAllAccounts();
    }

    @Test
    @Order(3)
    public void addAccount() throws Exception {
        BankAccount bankAccount = new BankAccount("IFSC0001234",5000,SAVINGS,null);
        bankAccount.setAccountNo(99999L);

        when(bankAccountService.addAccount(bankAccount)).thenReturn(bankAccount);

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody=mapper.writeValueAsString(bankAccount);

        this.mockMvc.perform(post("/api/accounts/admin/addAccount")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNo").value(99999))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ifsc").value("IFSC0001234"))
                .andDo(print());
        Mockito.verify(bankAccountService).addAccount(bankAccount);
    }

    @Test
    @Order(4)
    public void updateAccount() throws Exception {
        BankAccount bankAccount = new BankAccount("IFSC0001234",5000,SAVINGS,null);
        bankAccount.setAccountNo(99999L);

        when(bankAccountService.updateAccount(bankAccount)).thenReturn(bankAccount);

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody=mapper.writeValueAsString(bankAccount);

        this.mockMvc.perform(put("/api/accounts/admin/updateAccount")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNo").value(99999))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ifsc").value("IFSC0001234"))
                .andDo(print());
        Mockito.verify(bankAccountService).updateAccount(bankAccount);
    }

    @Test
    @Order(5)
    public void deleteAccount() throws Exception {
        doNothing().when(bankAccountService).deleteAccount(45678L);

        this.mockMvc.perform(delete("/api/accounts/admin/{accountNumber}",45678))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
        verify(bankAccountService).deleteAccount(45678L);
    }
}
