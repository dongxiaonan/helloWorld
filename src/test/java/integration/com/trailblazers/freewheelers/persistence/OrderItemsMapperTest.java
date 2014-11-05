package integration.com.trailblazers.freewheelers.persistence;

import com.trailblazers.freewheelers.mappers.ItemMapper;
import com.trailblazers.freewheelers.mappers.OrderItemsMapper;
import com.trailblazers.freewheelers.mappers.ReserveOrderMapper;
import com.trailblazers.freewheelers.model.*;
import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class OrderItemsMapperTest extends MapperTestBase {

    @Mock
    SqlSession sqlSession;

    private OrderItemsMapper orderItemsMapper;

    private Item item =  new Item()
            .setDescription("")
            .setName("")
            .setPrice(BigDecimal.valueOf(0))
            .setQuantity(0l)
            .setItemId(7563l)
            .setType(ItemType.ACCESSORIES);

    private ReserveOrder reserveOrder = new ReserveOrder()
            .addItemToOrder(item.getItemId(), 1l)
            .setAccount_id(2l)
            .setNote("")
            .setOrder_id(7659l)
            .setReservation_timestamp(new Date());
    private ItemMapper itemMapper;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        orderItemsMapper = getSqlSession().getMapper(OrderItemsMapper.class);
        itemMapper = getSqlSession().getMapper(ItemMapper.class);
        ReserveOrderMapper reserveOrderMapper = getSqlSession().getMapper(ReserveOrderMapper.class);
        itemMapper.insert(item);
        reserveOrderMapper.insert(reserveOrder);
    }

    @Test
    public void testSaveOrderItems() throws Exception {
        assertThat(orderItemsMapper.saveOrderItems(reserveOrder.getOrder_id(), new OrderItem(item.getItemId(), 1l)), is(1));
    }

    @Test
    public void testGetItemsByOrderId() throws Exception {
        itemMapper.insert(item.setItemId(item.getItemId() + 1));
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        orderItems.add(new OrderItem(item.getItemId(), 1l));
        orderItemsMapper.saveOrderItems(reserveOrder.getOrder_id(), new OrderItem(item.getItemId(), 1l));

        assertThat(orderItemsMapper.getItemsByOrderId(reserveOrder.getOrder_id()), is(orderItems));
    }

    @Test
    public void testUpdateQuantity() throws Exception {
        List<OrderItem> expectedOrderItems = new ArrayList<OrderItem>();
        expectedOrderItems.add(new OrderItem(item.getItemId(), 5l));
        orderItemsMapper.saveOrderItems(reserveOrder.getOrder_id(), new OrderItem(item.getItemId(), 1l));

        assertThat(orderItemsMapper.updateQuantity(reserveOrder.getOrder_id(), new OrderItem(item.getItemId(), 5l)), is(1));
        assertThat(orderItemsMapper.getItemsByOrderId(reserveOrder.getOrder_id()),is(expectedOrderItems));
    }

    @Test
    public void shouldDeleteAnOrderItemByItemId() throws Exception {
        orderItemsMapper.saveOrderItems(reserveOrder.getOrder_id(), new OrderItem(item.getItemId(), 1l));

        assertThat(orderItemsMapper.deleteByItemId(item.getItemId()), is(1));
        assertThat(orderItemsMapper.getItemsByOrderId(reserveOrder.getOrder_id()).size(),is(0));
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

}