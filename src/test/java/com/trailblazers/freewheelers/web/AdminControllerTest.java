package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.model.*;
import com.trailblazers.freewheelers.service.AccountService;
import com.trailblazers.freewheelers.service.ItemService;
import com.trailblazers.freewheelers.service.ReserveOrderService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ExtendedModelMap;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminControllerTest {

    @Mock
    private ReserveOrderService reserveOrderService;
    @Mock
    private AccountService accountService;
    @Mock
    private ItemService itemService;

    @InjectMocks
    private AdminController adminController;

    @Test
    public void shouldAddAttributeToModelWhenGetIsCalled() throws Exception {
        ExtendedModelMap expectedModelMap = new ExtendedModelMap();

        adminController.get(expectedModelMap);

        assertTrue(expectedModelMap.containsAttribute("reserveOrders"));
    }

    @Test
    public void shouldReturnAllOrders() throws Exception {
        OrderItem orderItem = new OrderItem(1l, 1l);
        List<OrderItem> orderItemList = new ArrayList<OrderItem>();
        orderItemList.add(orderItem);
        ReserveOrder reserveOrder = new ReserveOrder().setAccount_id(1l).setOrderItems(orderItemList);
        List<ReserveOrder> reserveOrderList = new ArrayList<ReserveOrder>();
        reserveOrderList.add(reserveOrder);
        Item item = new Item();
        Account account = new Account();
        account.setAccount_id(1l);
        List <Item> itemList = new ArrayList<Item>();
        itemList.add(item);

        when(reserveOrderService.getAllOrdersByAccount()).thenReturn(reserveOrderList);
        when(accountService.get(1l)).thenReturn(account);
        when(itemService.getById(1l)).thenReturn(item);

        assertThat(adminController.getAllOrders().get(0).getAccount().getAccount_id(), is(1l));
    }

}