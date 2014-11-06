package com.trailblazers.freewheelers.web;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class LoginControllerTest {

    LoginController loginController;

    @Before
    public void setUp() {
        loginController = new LoginController();
    }

    @Test
    public void shouldReturnStringLoginWhenCalled() throws Exception {
        String result = loginController.login(mock(Model.class));

        assertEquals(result, "login");
    }

    @Test
    public void shouldReturnErrorStringLoginWhenCalled() throws Exception {
        ExtendedModelMap expectedModelMap = new ExtendedModelMap();

        String result = loginController.loginError(expectedModelMap);

        assertEquals(result, "login");
        assertTrue(expectedModelMap.containsAttribute("error"));
    }

    @Test
    public void shouldReturnStringLogoutWhenCalled() throws Exception {
        String result = loginController.logout(mock(Model.class));

        assertEquals(result, "logout");
    }

    @Test
    public void shouldReturnStringAccessDeniedWhenCalled() throws Exception {
        String result = loginController.accessDenied();

        assertEquals(result, "accessDenied");
    }
}