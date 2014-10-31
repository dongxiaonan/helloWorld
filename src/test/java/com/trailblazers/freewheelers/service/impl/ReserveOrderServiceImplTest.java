package com.trailblazers.freewheelers.service.impl;

import com.trailblazers.freewheelers.mappers.ReserveOrderMapper;
import com.trailblazers.freewheelers.model.ReserveOrder;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class ReserveOrderServiceImplTest {

    ReserveOrderServiceImpl reserveOrderService;


    ReserveOrderMapper reserveOrderMapper;


    SqlSession sqlSession;

    @Before
    public void setUp() throws Exception {
        reserveOrderService = new ReserveOrderServiceImpl();
        reserveOrderMapper = mock(ReserveOrderMapper.class);
        sqlSession = mock(SqlSession.class);
        reserveOrderService.orderMapper = reserveOrderMapper;
        reserveOrderService.sqlSession = sqlSession;
    }

    @Test
    public void shouldReturnAOrderGivenAOrderId() {
        ReserveOrder expectedOrder = new ReserveOrder();
        expectedOrder.setOrder_id(2l);
        when(reserveOrderMapper.get(2l)).thenReturn(expectedOrder);

        reserveOrderService.getOrderById(expectedOrder.getOrder_id());

        assertEquals(reserveOrderService.getOrderById(expectedOrder.getOrder_id()), expectedOrder);
    }

}