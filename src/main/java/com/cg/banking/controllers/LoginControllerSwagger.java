package com.cg.banking.controllers;

import com.cg.banking.beans.ErrorResponse;
import com.cg.banking.jwt.MobileNumberAndPasswordAuthenticationRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("Login")
@RequestMapping("/")
@RestController
public class LoginControllerSwagger {
    //Only for swagger to show this end point as it is by default implemented and overidden by Spring Security
    @ApiOperation(value = "Login",notes ="Login with your mobile number and password")
    @ApiResponses({@ApiResponse(code = 200, message = "", response = Authentication.class),@ApiResponse(code = 400,message = "error",response = ErrorResponse.class)})
    @PostMapping("/login")
    public void login(@RequestBody MobileNumberAndPasswordAuthenticationRequest authenticationRequest) {
        throw new IllegalStateException("Spring Security handles this end point");
    }
}
