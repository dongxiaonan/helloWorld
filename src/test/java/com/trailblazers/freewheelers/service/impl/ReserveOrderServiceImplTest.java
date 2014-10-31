package com.trailblazers.freewheelers.service.impl;

import com.trailblazers.freewheelers.mappers.ReserveOrderMapper;
import com.trailblazers.freewheelers.model.ReserveOrder;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ReserveOrderServiceImplTest {

    ReserveOrderServiceImpl reserveOrderService;

    @Mock
    ReserveOrderMapper reserveOrderMapper;

    @Mock
    SqlSession sqlSession;

    @Before
    public void setUp() throws Exception {
        when(sqlSession.getMapper(ReserveOrderMapper.class)).thenReturn(reserveOrderMapper);
       reserveOrderService= new ReserveOrderServiceImpl(sqlSession);
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