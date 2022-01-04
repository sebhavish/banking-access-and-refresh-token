package com.cg.banking.controllers;

import com.cg.banking.beans.Roles;
import com.cg.banking.beans.User;
import com.cg.banking.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {UserControllerMockTest.class})
public class UserControllerMockTest {
    @Mock
    UserService userService;
    @InjectMocks
    UserController userController;
    @Mock
    Authentication auth;

    @Test
    public void getUser() {
        User user = new User("9988776655","bhavish","password",null, Roles.ROLE_USER);
        UserController userControllerSpy = Mockito.spy(userController);
        Mockito.doReturn(false).when(userControllerSpy).isRole("ROLE_USER");
        Mockito.when(userService.getUser("9988776655")).thenReturn(Optional.of(user));
        Optional<User> returnedUser = userControllerSpy.getUser("9988776655",auth);
        assertEquals(user,returnedUser.get());
        Mockito.verify(userService).getUser("9988776655");
    }

    @Test
    public void getUser2() {
        User user = new User("9988776655","bhavish","password",null,Roles.ROLE_USER);
        UserController userControllerSpy = Mockito.spy(userController);
        Mockito.doReturn(true).when(userControllerSpy).isRole("ROLE_USER");
        Mockito.when(auth.getName()).thenReturn("9988776655");
        Mockito.when(userService.getUser("9988776655")).thenReturn(Optional.of(user));
        Optional<User> returnedUser = userControllerSpy.getUser("99887766555",auth);
        assertEquals(user,returnedUser.get());
        Mockito.verify(userService).getUser("9988776655");
    }

    @Test
    public void isRole() {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        grantedAuthorities.add(grantedAuthority);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        Mockito.doReturn(grantedAuthorities).when(auth).getAuthorities();
        assertEquals(true,userController.isRole("ROLE_USER"));
        assertEquals(false,userController.isRole("ROLE_ADMIN"));
        Mockito.verify(auth,Mockito.times(2)).getAuthorities();
    }
}
