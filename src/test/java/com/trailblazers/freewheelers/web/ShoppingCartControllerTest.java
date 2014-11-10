package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.FreeWheelersServer;
import com.trailblazers.freewheelers.model.*;
import com.trailblazers.freewheelers.service.AccountService;
import com.trailblazers.freewheelers.service.ItemService;
import com.trailblazers.freewheelers.service.ReserveOrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
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
        item.setItemId(739L).setPrice(BigDecimal.valueOf(5.22)).setQuantity(1l);
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
            Item item1 = getSomeItem().setPrice(BigDecimal.valueOf(5.78));
            expectedItemsInCart.add(item1);
            HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
            MockHttpSession httpSession = new MockHttpSession();
            ExtendedModelMap expectedModelMap = new ExtendedModelMap();
            BigDecimal expectedTotalPrice = BigDecimal.valueOf(10.0).setScale(2, BigDecimal.ROUND_HALF_UP);

            when(itemService.getById(item1.getItemId())).thenReturn(item1);
            when(httpServletRequest.getSession()).thenReturn(httpSession);

            shoppingCartController.addToShoppingCart(expectedModelMap, item1, httpServletRequest);
            Item item2 = getSomeItem().setItemId(item1.getItemId() + 1).setPrice(BigDecimal.valueOf(4.22));
            when(itemService.getById(item2.getItemId())).thenReturn(item2);
            expectedItemsInCart.add(item2);

            shoppingCartController.addToShoppingCart(expectedModelMap, item2, httpServletRequest);

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
        items.add(new OrderItem(item.getItemId(), 2L));
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
        verify(itemService, times(1)).decreaseQuantity(item, 2l);
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
    public void shouldReturnAErrorMessageWhenTheItemHaveLessThenZeroQuantity() {
        Item item = getSomeItem().setQuantity(0l);
        ArrayList<Item> itemList = new ArrayList<Item>();
        itemList.add(getSomeItem());
        ExtendedModelMap expectedModelMap = new ExtendedModelMap();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession mockHttpSession = mock(HttpSession.class);
        when(request.getSession()).thenReturn(mockHttpSession);
        when(accountService.getAccountByName(anyString())).thenReturn(new Account());
        when(itemService.getById(item.getItemId())).thenReturn(item);
        when(mockHttpSession.getAttribute("totalCartPrice")).thenReturn((Object) BigDecimal.ONE);

        shoppingCartController.checkoutItem(expectedModelMap, mock(Principal.class), itemList, request);

        assertThat((String) expectedModelMap.asMap().get("quantityErrorMessage"), is("Sorry, some items are no longer available and have been removed from your cart."));
    }

    @Test
    public void shouldReturnToHomePageWhenAnItemIsAddedToShoppingCart() throws Exception {
        Item item = new Item();
        item.setItemId(739L).setPrice(BigDecimal.valueOf(5.22)).setQuantity(1l);
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

    @Test
    public void userCannotAddAnItemToCartIfItIsNotAvailableWhileAddingMultipleItems() throws Exception {
        FreeWheelersServer.enabledFeatures.add("multipleItemsPerCart");
        Item cartItem = getSomeItem();
        Item itemInDB =getSomeItem().setQuantity(0l);
        when(itemService.getById(itemInDB.getItemId())).thenReturn(itemInDB);
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        MockHttpSession httpSession = new MockHttpSession();
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        ExtendedModelMap expectedModelMap = new ExtendedModelMap();
        when(accountService.getAccountByName(anyString())).thenReturn(new Account());

        String result = shoppingCartController.addToShoppingCart(expectedModelMap,cartItem,httpServletRequest);

        assertThat(result,is("redirect:/"));
        assertEquals(true, httpServletRequest.getSession().getAttribute("error"));
        assertEquals(cartItem, httpServletRequest.getSession().getAttribute("lastItem"));
    }

    @Test
    public void shouldNotUpdateDatabaseInCaseOfOneOrMoreItemsNotAvailable() throws Exception {
        Item testItem = getSomeItem();
        Item cartItem = getSomeItem().setQuantity(2l);
        when(itemService.getById(7595l)).thenReturn(testItem);
        List<Item> items =new ArrayList<Item>(Arrays.asList(cartItem));
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpSession httpSession = mock(HttpSession.class);
        Principal principal = mock(Principal.class);
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute("sessionItems")).thenReturn(items);
        ExtendedModelMap expectedModelMap = new ExtendedModelMap();
        when(accountService.getAccountByName(anyString())).thenReturn(new Account());

        String result = shoppingCartController.checkoutItem(expectedModelMap,principal,new ArrayList<Item>(),httpServletRequest);

        verify(itemService,times(0)).decreaseQuantity(any(Item.class),anyLong());
        verify(httpSession).setAttribute("sessionItems", new ArrayList<Item>(Arrays.asList(testItem)));
        assertThat(result, is("/shoppingCart/myShoppingCart"));
    }

    private Item getSomeItem() {
        return new Item().setQuantity(1L)
                .setItemId(7595l)
                .setType(ItemType.ACCESSORIES)
                .setPrice(BigDecimal.TEN)
                .setName("Test Item")
                .setDescription(" ");
    }
}