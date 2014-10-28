package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.model.Account;
import com.trailblazers.freewheelers.model.Item;
import com.trailblazers.freewheelers.model.ReserveOrder;
import com.trailblazers.freewheelers.service.AccountService;
import com.trailblazers.freewheelers.service.ItemService;
import com.trailblazers.freewheelers.service.ReserveOrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import sun.security.acl.PrincipalImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
@RunWith(MockitoJUnitRunner.class)
public class ShoppingCartControllerTest {

    @Mock
    private ReserveOrderService reserveOrderService;
    @Mock
    private AccountService accountService;
    @Mock
    private ItemService itemService;

    @InjectMocks
    private ShoppingCartController shoppingCartController;

    @Test
    public void shouldSaveItemIntoSessionWhenReserveItemIsCalled (){
        Item item = new Item();
        item.setItemId(739L);
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        MockHttpSession httpSession = new MockHttpSession();
        ExtendedModelMap expectedModelMap = new ExtendedModelMap();

        when(itemService.getById(739L)).thenReturn(item);
        when(httpServletRequest.getSession()).thenReturn(httpSession);

        shoppingCartController.reserveItem(expectedModelMap, item, httpServletRequest);

        assertTrue(expectedModelMap.containsValue(item));
        assertThat(httpServletRequest.getSession().getAttribute("sessionItem"), is((Object)item));

    }

    @Test
    public void shouldCallReserveOrderServiceWhenCheckoutItemIsCalled(){
        Model model = mock(Model.class);
        Principal principle = new PrincipalImpl("UserCat");
        Item item = new Item();
        item.setItemId(739L);
        Account account = new Account();
        account.setAccount_id(2L);
        ReserveOrder reserveOrder = new ReserveOrder(2L, 739L, new Date());

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpSession httpSession = new MockHttpSession();
        HttpSession spy = spy(httpSession);
        spy.setAttribute("sessionItem", item);

        when(httpServletRequest.getSession()).thenReturn(spy);

        when(itemService.getById(739L)).thenReturn(item);
        when(accountService.getAccountByName("UserCat")).thenReturn(account);


        shoppingCartController.checkoutItem(model, principle, item, httpServletRequest);

        verify(reserveOrderService, times(1)).save(reserveOrder);
        verify(itemService, times(1)).decreaseQuantityByOne(item);
        assertThat(httpServletRequest.getSession().getAttribute("sessionItem"), is(nullValue()));
    }

    @Test
    public void shouldClearItemFromSessionWhenClearCartIsClicked (){
        Item item = new Item();
        item.setItemId(739L);
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpSession httpSession = new MockHttpSession();
        HttpSession spy = spy(httpSession);
        spy.setAttribute("sessionItem", item);

        when(httpServletRequest.getSession()).thenReturn(spy);

        String redirect = shoppingCartController.clearShoppingCart(httpServletRequest);

        assertThat(httpServletRequest.getSession().getAttribute("sessionItem"), is(nullValue()));
        assertEquals(redirect, "redirect:/");

    }
}