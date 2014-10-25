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
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.web.portlet.ModelAndView;
import sun.security.acl.PrincipalImpl;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;

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
    public void shouldCallReserveOrderServiceWhenCheckoutItemIsCalled(){

        Model model = mock(Model.class);
        Principal principle = new PrincipalImpl("UserCat");
        Item item = new Item();
        item.setItemId(739L);
        Account account = new Account();
        account.setAccount_id(2L);
        ReserveOrder reserveOrder = new ReserveOrder(2L, 739L, new Date());
        when(itemService.getById(739L)).thenReturn(item);
        when(accountService.getAccountIdByName("UserCat")).thenReturn(account);

        shoppingCartController.checkoutItem(model, principle, item);

        verify(reserveOrderService, times(1)).save(reserveOrder);
        verify(itemService, times(1)).decreaseQuantityByOne(item);
    }
}