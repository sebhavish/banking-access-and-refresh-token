package com.cg.banking.security;

import com.cg.banking.jwt.ExceptionHandlerFilter;
import com.cg.banking.jwt.JwtConfig;
import com.cg.banking.jwt.JwtTokenVerifier;
import com.cg.banking.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.cg.banking.service.MyUserDetailsService;
import com.cg.banking.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)//Disabling method security as we don't need it currently for our project
public class BankingSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private final MyUserDetailsService myUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public BankingSecurityConfig(MyUserDetailsService myUserDetailsService, PasswordEncoder passwordEncoder, SecretKey secretKey, JwtConfig jwtConfig, ExceptionHandlerFilter exceptionHandlerFilter, RefreshTokenService refreshTokenService) {
        this.myUserDetailsService = myUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
        this.exceptionHandlerFilter = exceptionHandlerFilter;
        this.refreshTokenService = refreshTokenService;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(exceptionHandlerFilter,JwtUsernameAndPasswordAuthenticationFilter.class)
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey,refreshTokenService))
                .addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfig), JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/api/home").permitAll()
                .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll()
                .antMatchers("/api/refresh/**").permitAll()
                .antMatchers("/api/users/admin/**").hasRole("ADMIN")
                .antMatchers("/api/accounts/admin/**").hasRole("ADMIN")
                .antMatchers("/api/users/user/**").hasAnyRole("ADMIN","USER")
                .antMatchers("/api/accounts/user/**").hasAnyRole("ADMIN","USER")
                .anyRequest()
                .authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(myUserDetailsService);
        return provider;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
