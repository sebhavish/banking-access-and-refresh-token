package com.cg.banking.controllers;

import com.cg.banking.beans.*;
import com.cg.banking.exception.TokenRefreshException;
import com.cg.banking.jwt.JwtConfig;
import com.cg.banking.service.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ComponentScan(basePackages = "com.cg.banking")
@AutoConfigureMockMvc
@ContextConfiguration
@SpringBootTest(classes = {AuthenticationControllerTest.class})
public class AuthenticationControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Mock
    RefreshTokenService refreshTokenService;
    @Spy
    JwtConfig jwtConfig;

    @InjectMocks
    AuthenticationController authenticationController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    @Order(1)
    public void refreshToken() throws Exception {
        //used to set field secret key as it is giving error while running test case
        ReflectionTestUtils.setField(authenticationController, "secretKey", Keys.hmacShaKeyFor("galgalgahglahglaghlaghalghalghalghlghlaghalg".getBytes()));
        User user = new User("9988776655","bhavish","password",null, Roles.ROLE_ADMIN);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(1);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(LocalDate.now().plusDays(4));
        refreshToken.setToken("ghgalghaagagpaghgagl");

        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("ghgalghaagagpaghgagl");
        RefreshTokenRequest wrongRequest = new RefreshTokenRequest();
        wrongRequest.setRefreshToken("ghalgha");
        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(request);

        Mockito.when(jwtConfig.getTokenExpirationAfterDays()).thenReturn(14);
        Mockito.when(refreshTokenService.findByToken("ghgalghaagagpaghgagl")).thenReturn(of(refreshToken));
        Mockito.when(refreshTokenService.verifyExpiration(refreshToken)).thenReturn(refreshToken);


        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/refresh/refreshToken")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").value("ghgalghaagagpaghgagl"))
                .andDo(print());
        assertEquals(HttpStatus.OK,authenticationController.refreshToken(request).getStatusCode());
        assertThrows(TokenRefreshException.class,()->authenticationController.refreshToken(wrongRequest));
        Mockito.verify(refreshTokenService,Mockito.times(3)).findByToken(any());
    }

    @Test
    @Order(2)
    public void logOut() throws Exception {
        LogOutRequest logOutRequest = new LogOutRequest();
        logOutRequest.setMobileNumber("9988776655");

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(logOutRequest);

        Mockito.when(refreshTokenService.deleteByMobileNumber("9988776655")).thenReturn(1);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/refresh/logout")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().string("Deleted Refresh Token Successfully and Logged Out"))
                .andDo(print());

    }
}
