package com.trailblazers.freewheelers.service.impl;

import com.trailblazers.freewheelers.mappers.MyBatisUtil;
import com.trailblazers.freewheelers.mappers.OrderItemsMapper;
import com.trailblazers.freewheelers.mappers.ReserveOrderMapper;
import com.trailblazers.freewheelers.model.OrderItem;
import com.trailblazers.freewheelers.model.OrderStatus;
import com.trailblazers.freewheelers.model.ReserveOrder;
import com.trailblazers.freewheelers.service.ReserveOrderService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReserveOrderServiceImpl implements ReserveOrderService {

    private final SqlSession sqlSession;
    private final ReserveOrderMapper orderMapper;
    private final OrderItemsMapper orderItemsMapper;

    public ReserveOrderServiceImpl() {
        sqlSession = MyBatisUtil.getSqlSessionFactory().openSession();
        orderMapper = sqlSession.getMapper(ReserveOrderMapper.class);
        orderItemsMapper = sqlSession.getMapper(OrderItemsMapper.class);
    }

    public ReserveOrderServiceImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
        this.orderMapper = sqlSession.getMapper(ReserveOrderMapper.class);
        this.orderItemsMapper = sqlSession.getMapper(OrderItemsMapper.class);
    }

    public void save(ReserveOrder reserveOrder) {
        if (reserveOrder.getOrder_id() == null) {
            orderMapper.insert(reserveOrder);
        } else {
            orderMapper.save(reserveOrder);
        }
        saveOrders(reserveOrder);
    }

    private void saveOrders(ReserveOrder reserveOrder) {
        for (OrderItem orderItem : reserveOrder.getOrderItems()) {
            orderItemsMapper.saveOrderItems(reserveOrder.getOrder_id(), orderItem);
        }
        sqlSession.commit();
    }

    public List<ReserveOrder> findAllOrdersByAccountId(Long account_id) {
        sqlSession.clearCache();
        return orderMapper.findAllFor(account_id);
    }

    public List<ReserveOrder> getAllOrdersByAccount() {
        sqlSession.clearCache();
        return orderMapper.findAll();
    }

    public void updateOrderDetails(Long order_id, OrderStatus status, String note) {
        ReserveOrder order = orderMapper.get(order_id);

        order.setStatus(status);
        order.setNote(note);

        orderMapper.save(order);
        sqlSession.commit();
    }

    @Override
    public ReserveOrder getOrderById(Long orderId) {
        return orderMapper.get(orderId);
    }

}
