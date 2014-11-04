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
import static org.junit.Assert.*;
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
        Principal principal = new PrincipalImpl("UserCat");
        Account account = new Account();
        account.setEncrypted(true);

        when(accountService.getAccountByName(Mockito.anyString())).thenReturn(account);
        when(itemService.getById(739L)).thenReturn(item);
        when(httpServletRequest.getSession()).thenReturn(httpSession);

        shoppingCartController.addToShoppingCart(expectedModelMap, item, httpServletRequest, principal);

        assertTrue(expectedModelMap.containsValue(item));
        assertThat(httpServletRequest.getSession().getAttribute("sessionItem"), is((Object)item));

    }

    @Test
    public void shouldCallReserveOrderServiceWhenCheckoutItemIsCalled(){
        Model model = mock(Model.class);
        Principal principle = new PrincipalImpl("UserCat");
        Item item = new Item();
        item.setItemId(739l);
        item.setQuantity(2l);
        Account account = new Account();
        account.setAccount_id(2l).setEncrypted(true);
        ReserveOrder reserveOrder = new ReserveOrder(2l, 739l, new Date());
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpSession httpSession = new MockHttpSession();
        HttpSession spy = spy(httpSession);
        spy.setAttribute("sessionItem", item);
        when(httpServletRequest.getSession()).thenReturn(spy);
        when(itemService.getById(739l)).thenReturn(item);
        when(accountService.getAccountByName("UserCat")).thenReturn(account);
        when(itemService.checkItemsQuantityIsMoreThanZero(739l)).thenReturn(true);

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

    @Test
    public void shouldCheckIfItemIsAvailableBeforeSavingOrder() {
        Item item = new Item().setItemId(2l);
        when(accountService.getAccountByName(anyString())).thenReturn(new Account().setEncrypted(true));
        when(itemService.getById(2l)).thenReturn(new Item());
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(new MockHttpSession());

        shoppingCartController.checkoutItem(mock(Model.class), new PrincipalImpl("UserCat"), item, request);

        verify(itemService).checkItemsQuantityIsMoreThanZero(2l);
    }

    @Test
    public void shouldReturnAErrorMessageWhenTheItemHaveLessThenZeroQuantity() {
        Item item = new Item().setItemId(2l);
        Principal principal = new PrincipalImpl("UserCat");
        ExtendedModelMap expectedModelMap = new ExtendedModelMap();
        HttpServletRequest request = mock(HttpServletRequest.class);
        Account account = new Account();
        account.setEncrypted(true);

        when(request.getSession()).thenReturn(new MockHttpSession());
        when(accountService.getAccountByName(anyString())).thenReturn(account);
        when(itemService.getById(2l)).thenReturn(new Item());
        when(itemService.checkItemsQuantityIsMoreThanZero(2l)).thenReturn(false);

        shoppingCartController.checkoutItem(expectedModelMap, principal, item, request);

        assertThat((String) expectedModelMap.asMap().get("quantityErrorMessage"), is("Sorry, item is no longer available."));
    }

    @Test
    public void shouldReturnToHomePageWhenAnItemIsAddedToShoppingCart() throws Exception {
        Item item = new Item();
        item.setItemId(739L);
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        MockHttpSession httpSession = new MockHttpSession();
        ExtendedModelMap expectedModelMap = new ExtendedModelMap();
        Principal principal = new PrincipalImpl("UserCat");
        Account account = new Account();
        account.setEncrypted(true);

        when(accountService.getAccountByName(Mockito.anyString())).thenReturn(account);
        when(itemService.getById(739L)).thenReturn(item);
        when(httpServletRequest.getSession()).thenReturn(httpSession);

        String result = shoppingCartController.addToShoppingCart(expectedModelMap, item, httpServletRequest, principal);

        assertTrue(expectedModelMap.containsValue(item));
        assertThat(httpServletRequest.getSession().getAttribute("sessionItem"), is((Object)item));
        assertThat(result,is("redirect:/?q=t"));

    }

    @Test
    public void shouldDisplayErrorMessageWhenInactiveUserTriesToAddItemToShoppingCart() throws Exception {
        Item item = new Item().setItemId(66l);
        Principal principal = new PrincipalImpl("UserCat");
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        MockHttpSession httpSession = new MockHttpSession();
        ExtendedModelMap expectedModelMap = new ExtendedModelMap();
        Account account = new Account();
        account.setEncrypted(false);

        when(accountService.getAccountByName(Mockito.anyString())).thenReturn(account);
        when(itemService.getById(66L)).thenReturn(item);
        when(httpServletRequest.getSession()).thenReturn(httpSession);

        String result = shoppingCartController.addToShoppingCart(expectedModelMap, item, httpServletRequest, principal);

        assertThat((String) expectedModelMap.asMap().get("encryptedErrorMessage"), is("Sorry, you need to reset your password first."));
        assertThat(result,is("redirect:/?q=t"));

    }

    @Test
    public void shouldDisplayErrorMessageWhenInactiveUserTriesToCheckoutShoppingCart() throws Exception {
        Item item = new Item().setItemId(66l);
        Principal principal = new PrincipalImpl("UserCat");
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        MockHttpSession httpSession = new MockHttpSession();
        ExtendedModelMap expectedModelMap = new ExtendedModelMap();
        Account account = new Account();
        account.setEncrypted(false);

        when(accountService.getAccountByName(Mockito.anyString())).thenReturn(account);
        when(itemService.getById(66L)).thenReturn(item);
        when(httpServletRequest.getSession()).thenReturn(httpSession);

        String result = shoppingCartController.checkoutItem(expectedModelMap, principal, item, mock(HttpServletRequest.class));

        assertThat((String) expectedModelMap.asMap().get("encryptedErrorMessage"), is("Sorry, you need to reset your password first."));
        assertThat(result,is("/shoppingCart/myShoppingCart"));

    }

}