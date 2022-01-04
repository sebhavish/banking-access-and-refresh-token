package com.cg.banking.service;

import com.cg.banking.beans.RefreshToken;
import com.cg.banking.exception.TokenRefreshException;
import com.cg.banking.repo.RefreshTokenRepository;
import com.cg.banking.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${application.jwt.refreshTokenExpirationAfterDays}")
    private Long refreshTokenDuration;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(String mobileNumber) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findById(mobileNumber).get());
        refreshToken.setExpiryDate(LocalDate.now().plusDays(refreshTokenDuration));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if(token.getExpiryDate().compareTo(LocalDate.now())<0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(),"Refresh Token was expired, please login again");
        }

        return token;
    }
    @Transactional
    public int deleteByMobileNumber(String mobileNumber) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(mobileNumber).get());
    }
}
