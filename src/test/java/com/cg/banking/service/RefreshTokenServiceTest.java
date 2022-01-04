package com.cg.banking.service;

import com.cg.banking.beans.RefreshToken;
import com.cg.banking.beans.Roles;
import com.cg.banking.beans.User;
import com.cg.banking.exception.TokenRefreshException;
import com.cg.banking.repo.RefreshTokenRepository;
import com.cg.banking.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {RefreshTokenServiceTest.class})
@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {
    @Mock
    RefreshTokenRepository refreshTokenRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    RefreshTokenService refreshTokenService;

    @Test
    public void findByToken() {
        User user = new User("9988776655","bhavish","password",null, Roles.ROLE_ADMIN);
        RefreshToken refreshToken = new RefreshToken();
        String token = "fjlglsdsglslgslggslgl";
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setId(1);
        refreshToken.setExpiryDate(LocalDate.now().plusDays(30));
        Mockito.when(refreshTokenRepository.findByToken(token)).thenReturn(of(refreshToken));
        RefreshToken returnedRefreshToken = refreshTokenService.findByToken(token).get();
        assertEquals(refreshToken,returnedRefreshToken);
        Mockito.verify(refreshTokenRepository).findByToken(token);
    }

    @Test
    public void createRefreshToken() {
        //ReflectionTestUtils is used to set field values because we can't able to fetch properties from properties file
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenDuration", 30L);
        User user = new User("9988776655","bhavish","password",null, Roles.ROLE_ADMIN);
        RefreshToken refreshToken = new RefreshToken();
        String token = "fjlglsdsglslgslggslgl";
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setId(1);
        refreshToken.setExpiryDate(LocalDate.now().plusDays(30));
        Mockito.when(userRepository.findById("9988776655")).thenReturn(of(user));
        Mockito.when(refreshTokenRepository.save(ArgumentMatchers.any(RefreshToken.class))).thenReturn(refreshToken);
        RefreshToken returnedRefreshToken = refreshTokenService.createRefreshToken("9988776655");
        assertEquals(refreshToken,returnedRefreshToken);
        Mockito.verify(refreshTokenRepository).save(ArgumentMatchers.any(RefreshToken.class));
        Mockito.verify(userRepository).findById("9988776655");
    }

    @Test
    public void verifyExpiration() {
        User user = new User("9988776655","bhavish","password",null, Roles.ROLE_ADMIN);
        RefreshToken refreshToken = new RefreshToken();
        String token = "fjlglsdsglslgslggslgl";
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setId(1);
        refreshToken.setExpiryDate(LocalDate.now().plusDays(30));
        Mockito.doNothing().when(refreshTokenRepository).delete(refreshToken);
        RefreshToken returnedRefreshToken = refreshTokenService.verifyExpiration(refreshToken);
        assertEquals(refreshToken,returnedRefreshToken);
        refreshToken.setExpiryDate(LocalDate.now().minusDays(1));
        assertThrows(TokenRefreshException.class,()->refreshTokenService.verifyExpiration(refreshToken));
        Mockito.verify(refreshTokenRepository).delete(refreshToken);
    }

    @Test
    public void deleteByMobileNumber() {
        User user = new User("9988776655","bhavish","password",null, Roles.ROLE_ADMIN);
        Mockito.when(userRepository.findById("9988776655")).thenReturn(of(user));
        Mockito.when(refreshTokenRepository.deleteByUser(user)).thenReturn(1);
        assertEquals(1,refreshTokenService.deleteByMobileNumber("9988776655"));
        Mockito.verify(refreshTokenRepository).deleteByUser(user);
    }
}
