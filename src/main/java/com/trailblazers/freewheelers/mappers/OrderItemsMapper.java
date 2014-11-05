package com.trailblazers.freewheelers.mappers;

import com.trailblazers.freewheelers.model.OrderItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface OrderItemsMapper {

    @Insert(
            "INSERT INTO orderItems (orderid, itemid,quantity) " +
                    "VALUES (#{orderId}, #{orderItem.itemId}, #{orderItem.quantity})"
    )
    int saveOrderItems(@Param("orderId") Long orderId,@Param("orderItem") OrderItem orderItem);

    @Select(
            "SELECT itemId,quantity from orderItems " +
                    "WHERE orderId=#{orderId}"
    )
    @ConstructorArgs({
            @Arg(column = "itemId", javaType = Long.class),
            @Arg(column = "quantity", javaType = Long.class)
    })
    List<OrderItem> getItemsByOrderId(@Param("orderId") Long orderId);

    @Update(
            "UPDATE orderItems " +
                    "SET quantity=#{orderItem.quantity} " +
                    "WHERE orderId=#{orderId} AND itemId=#{orderItem.itemId}"
    )
    int updateQuantity(@Param("orderId") Long orderId,@Param("orderItem") OrderItem orderItem);

    @Delete(
            "DELETE FROM orderItems " +
                    "WHERE itemId=#{itemId}"
    )
    int deleteByItemId(@Param("itemId") Long itemId);
}
