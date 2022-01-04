package com.cg.banking.service;

import com.cg.banking.beans.Roles;
import com.cg.banking.beans.User;
import com.cg.banking.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {MyUserDetailsServiceTest.class})
@ExtendWith(MockitoExtension.class)
public class MyUserDetailsServiceTest {

    @Mock
    UserRepository userRepository;
    @InjectMocks
    MyUserDetailsService myUserDetailsService;

    @Test
    public void loadUserByUsername() {
        User user = new User("9988776655","bhavish","password",null, Roles.ROLE_USER);
        Mockito.when(userRepository.findById("9988776655")).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById("985647")).thenReturn(Optional.empty());
        UserDetails userDetails = myUserDetailsService.loadUserByUsername("9988776655");
        assertEquals(user.getMobileNumber(),userDetails.getUsername());
        assertThrows(UsernameNotFoundException.class,()->myUserDetailsService.loadUserByUsername("985647"));
    }
}
