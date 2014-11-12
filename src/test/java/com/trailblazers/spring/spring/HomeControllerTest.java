package com.trailblazers.spring.spring;

import com.java.spring.spring.HomeController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ExtendedModelMap;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class HomeControllerTest {


    @InjectMocks
    private HomeController homeController= new HomeController();

    @Test
    public void shouldShowTheHomePage() {
        ExtendedModelMap model =new ExtendedModelMap();
        HttpServletRequest request = mock(HttpServletRequest.class);

        String result = homeController.get(model);

        assertThat(result, is("home"));
    }
}