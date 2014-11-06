package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.FreeWheelersServer;
import com.trailblazers.freewheelers.model.Account;
import com.trailblazers.freewheelers.model.Item;
import com.trailblazers.freewheelers.model.OrderItem;
import com.trailblazers.freewheelers.model.ReserveOrder;
import com.trailblazers.freewheelers.service.AccountService;
import com.trailblazers.freewheelers.service.ItemService;
import com.trailblazers.freewheelers.service.ReserveOrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import sun.security.acl.PrincipalImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;

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
        item.setItemId(739L).setPrice(BigDecimal.valueOf(5.22));
        List<Item> expectedItemList = new ArrayList<Item>();
        expectedItemList.add(item);
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        MockHttpSession httpSession = new MockHttpSession();
        ExtendedModelMap expectedModelMap = new ExtendedModelMap();

        when(itemService.getById(739L)).thenReturn(item);
        when(httpServletRequest.getSession()).thenReturn(httpSession);

        shoppingCartController.addToShoppingCart(expectedModelMap, item, httpServletRequest);

        assertTrue(expectedModelMap.containsValue(expectedItemList));
        assertThat(httpServletRequest.getSession().getAttribute("sessionItems"), is((Object)expectedItemList));
        assertThat(httpServletRequest.getSession().getAttribute("totalCartPrice"),is((Object)BigDecimal.valueOf(5.22)));

    }

    @Test
    public void shouldSaveMultipleItemsIntoSessionWhenAddToCartIsCalled (){
        if(FreeWheelersServer.enabledFeatures.contains("multipleItemsPerCart")) {
            List<Item> expectedItemsInCart = new ArrayList<Item>();
            Item item = new Item();
            item.setItemId(739L).setPrice(BigDecimal.valueOf(5.78));
            expectedItemsInCart.add(item);
            HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
            MockHttpSession httpSession = new MockHttpSession();
            ExtendedModelMap expectedModelMap = new ExtendedModelMap();
            BigDecimal expectedTotalPrice = BigDecimal.valueOf(10.0).setScale(2, BigDecimal.ROUND_HALF_UP);

            when(itemService.getById(anyLong())).thenReturn(item);
            when(httpServletRequest.getSession()).thenReturn(httpSession);

            shoppingCartController.addToShoppingCart(expectedModelMap, item, httpServletRequest);
            item.setItemId(750L).setPrice(BigDecimal.valueOf(4.22));
            expectedItemsInCart.add(item);

            shoppingCartController.addToShoppingCart(expectedModelMap, item, httpServletRequest);

            assertTrue(expectedModelMap.containsValue(expectedItemsInCart));
            assertThat(httpServletRequest.getSession().getAttribute("sessionItems"), is((Object) expectedItemsInCart));
            assertThat(httpServletRequest.getSession().getAttribute("totalCartPrice"), is((Object) expectedTotalPrice));
        }
    }

    @Test
    public void shouldCallReserveOrderServiceWhenCheckoutItemIsCalled(){
        Model model = mock(Model.class);
        Principal principle = new PrincipalImpl("UserCat");
        Item item = new Item();
        item.setItemId(739l);
        item.setQuantity(2l);
        ArrayList<Item> itemList = new ArrayList<Item>();
        itemList.add(item);
        List<OrderItem> items = new ArrayList<OrderItem>();
        items.add(new OrderItem(item.getItemId(), 1L));
        Account account = new Account();
        account.setAccount_id(2l);
        ReserveOrder reserveOrder = new ReserveOrder(2l, items, new Date());
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpSession httpSession = new MockHttpSession();
        HttpSession spy = spy(httpSession);
        spy.setAttribute("sessionItems", item);
        when(httpServletRequest.getSession()).thenReturn(spy);
        when(itemService.getById(739l)).thenReturn(item);
        when(accountService.getAccountByName("UserCat")).thenReturn(account);
        when(itemService.checkItemsQuantityIsMoreThanZero(739l)).thenReturn(true);

        shoppingCartController.checkoutItem(model, principle, itemList, httpServletRequest);

        verify(reserveOrderService, times(1)).save(reserveOrder);
        verify(itemService, times(1)).decreaseQuantityByOne(item);
        assertThat(httpServletRequest.getSession().getAttribute("sessionItems"), is(nullValue()));
    }

    @Test
    public void shouldClearItemFromSessionWhenClearCartIsClicked (){
        Item item = new Item();
        item.setItemId(739L);
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpSession httpSession = new MockHttpSession();
        HttpSession spy = spy(httpSession);
        spy.setAttribute("sessionItems", item);

        when(httpServletRequest.getSession()).thenReturn(spy);

        String redirect = shoppingCartController.clearShoppingCart(httpServletRequest);

        assertThat(httpServletRequest.getSession().getAttribute("sessionItems"), is(nullValue()));
        assertEquals(redirect, "redirect:/");
    }

    @Test
    public void shouldCheckIfItemIsAvailableBeforeSavingOrder() {
        Item item = new Item().setItemId(2l).setPrice(BigDecimal.ZERO);
        ArrayList<Item> itemList = new ArrayList<Item>();
        itemList.add(item);
        when(accountService.getAccountByName(anyString())).thenReturn(new Account());
        when(itemService.getById(2l)).thenReturn(new Item());
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession mockHttpSession = mock(HttpSession.class);
        when(request.getSession()).thenReturn(mockHttpSession);
        when(mockHttpSession.getAttribute("totalCartPrice")).thenReturn((Object) BigDecimal.ONE);

        shoppingCartController.checkoutItem(mock(Model.class), mock(Principal.class), itemList, request);

        verify(itemService).checkItemsQuantityIsMoreThanZero(2l);
    }

    @Test
    public void shouldReturnAErrorMessageWhenTheItemHaveLessThenZeroQuantity() {
        Item item = new Item().setItemId(2l).setPrice(BigDecimal.ZERO);
        ArrayList<Item> itemList = new ArrayList<Item>();
        itemList.add(item);
        ExtendedModelMap expectedModelMap = new ExtendedModelMap();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession mockHttpSession = mock(HttpSession.class);
        when(request.getSession()).thenReturn(mockHttpSession);
        when(accountService.getAccountByName(anyString())).thenReturn(new Account());
        when(itemService.checkItemsQuantityIsMoreThanZero(anyLong())).thenReturn(false);
        when(mockHttpSession.getAttribute("totalCartPrice")).thenReturn((Object) BigDecimal.ONE);

        shoppingCartController.checkoutItem(expectedModelMap, mock(Principal.class), itemList, request);

        assertThat((String) expectedModelMap.asMap().get("quantityErrorMessage"), is("Sorry, some items are no longer available and have been removed from your cart."));
    }

    @Test
    public void shouldReturnToHomePageWhenAnItemIsAddedToShoppingCart() throws Exception {
        Item item = new Item();
        item.setItemId(739L).setPrice(BigDecimal.valueOf(5.22));
        List<Item> expectedItemList = new ArrayList<Item>();
        expectedItemList.add(item);
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        MockHttpSession httpSession = new MockHttpSession();
        ExtendedModelMap expectedModelMap = new ExtendedModelMap();

        when(itemService.getById(739L)).thenReturn(item);
        when(httpServletRequest.getSession()).thenReturn(httpSession);

        String result = shoppingCartController.addToShoppingCart(expectedModelMap, item, httpServletRequest);

        assertTrue(expectedModelMap.containsValue(expectedItemList));
        assertThat(httpServletRequest.getSession().getAttribute("sessionItems"), is((Object)expectedItemList));
        assertThat(httpServletRequest.getSession().getAttribute("totalCartPrice"),is((Object)BigDecimal.valueOf(5.22)));
        assertThat(result,is("redirect:/"));

    }

}