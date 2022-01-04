package com.cg.banking.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeController {
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    @Autowired
//    private MyUserDetailsService userDetailsService;
    Logger logger = LoggerFactory.getLogger(HomeController.class);
    @GetMapping("/home")
    public String home() {
        logger.info("Welcome to home");
        return ("<h1>welcome</h1>");
    }

//    @PostMapping("/authenticate")
//    public ResponseEntity<?> createAuthenticationToken(@RequestBody MobileNumberAndPasswordAuthenticationRequest authRequest) throws Exception {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                    authRequest.getMobileNumber(),authRequest.getPassword()
//            ));
//        } catch (BadCredentialsException e) {
//            throw new Exception("Incorrect username or password");
//        }
//        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getMobileNumber());
//        return ResponseEntity.ok("user login successful");
//    }
}
