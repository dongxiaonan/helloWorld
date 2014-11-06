package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.model.Item;
import com.trailblazers.freewheelers.service.ItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ExtendedModelMap;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HomeControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private HomeController homeController= new HomeController();

    @Test
    public void shouldShowTheHomePage() {
        ExtendedModelMap model =new ExtendedModelMap();
        HttpServletRequest request = mock(HttpServletRequest.class);

        String result = homeController.get(model,new Item(),request);

        assertThat(result, is("home"));
    }
}