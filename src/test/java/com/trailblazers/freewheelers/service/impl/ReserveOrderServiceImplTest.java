package com.trailblazers.freewheelers.service.impl;

import com.trailblazers.freewheelers.mappers.ItemMapper;
import com.trailblazers.freewheelers.mappers.OrderItemsMapper;
import com.trailblazers.freewheelers.mappers.ReserveOrderMapper;
import com.trailblazers.freewheelers.model.Item;
import com.trailblazers.freewheelers.model.OrderItem;
import com.trailblazers.freewheelers.model.ReserveOrder;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ReserveOrderServiceImplTest {

    ReserveOrderServiceImpl reserveOrderService;

    @Mock
    ReserveOrderMapper reserveOrderMapper;

    @Mock
    SqlSession sqlSession;

    @Mock
    OrderItemsMapper orderItemsMapper;

    @Before
    public void setUp() throws Exception {
       when(sqlSession.getMapper(ReserveOrderMapper.class)).thenReturn(reserveOrderMapper);
       when(sqlSession.getMapper(OrderItemsMapper.class)).thenReturn(orderItemsMapper);
       reserveOrderService= new ReserveOrderServiceImpl(sqlSession);
    }

    @Test
    public void shouldReturnAOrderGivenAOrderId() {
        ReserveOrder expectedOrder = new ReserveOrder();
        expectedOrder.setOrder_id(2l);
        when(reserveOrderMapper.get(2l)).thenReturn(expectedOrder);

        assertEquals(reserveOrderService.getOrderById(expectedOrder.getOrder_id()), expectedOrder);
    }

    @Test
    public void shouldAddOrderItemsForAOrder() {
        ItemMapper mockItemMapper = mock(ItemMapper.class);
        mockItemMapper.insert(new Item().setItemId(15l));
        ReserveOrder expectedOrder = new ReserveOrder();
        expectedOrder.addItemToOrder(15l,1l);
        reserveOrderService.save(expectedOrder);

        verify(reserveOrderMapper).insert(expectedOrder);
        verify(orderItemsMapper).saveOrderItems(expectedOrder.getOrder_id(),new OrderItem(15l,1l));

    }

}