package com.cg.banking.controllers;

import com.cg.banking.beans.*;
import com.cg.banking.exception.TokenRefreshException;
import com.cg.banking.jwt.JwtConfig;
import com.cg.banking.service.RefreshTokenService;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Date;

@Api("Refresh And Logout")
@RequestMapping("/api/refresh")
@RestController
public class AuthenticationController {
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    private SecretKey secretKey;

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(MyUserDetails::new)
                .map(myUserDetails -> {
                    String token = Jwts.builder()
                            .setSubject(myUserDetails.getUsername())
                            .claim("authorities",myUserDetails.getAuthorities())
                            .setIssuedAt(new Date())
                            .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
//                            .signWith(Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes()))
                            .signWith(secretKey)
                            .compact();
                    return ResponseEntity.ok(new JwtResponse(token,refreshToken));
                        })
                .orElseThrow(()->new TokenRefreshException(refreshToken,"Refresh token is not in Database"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logOut(@RequestBody LogOutRequest logOutRequest) {
        refreshTokenService.deleteByMobileNumber(logOutRequest.getMobileNumber());
        return ResponseEntity.ok("Deleted Refresh Token Successfully and Logged Out");
    }
}
