package com.example.app.order.mapper;

import com.example.app.order.model.dto.request.OrderListQuery;
import com.example.app.order.model.entity.Order;
import com.example.app.order.model.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {
    List<Order> selectPage(OrderListQuery query);
    Long count(OrderListQuery query);
    Order selectById(@Param("id") String id);
    List<OrderItem> selectItemsByOrderId(@Param("orderId") String orderId);
    int insert(Order order);
    int insertItems(List<OrderItem> items);
    int updateByIdAndUpdatedAt(Order order);
    int logicDeleteByIdAndUpdatedAt(@Param("id") String id, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);
    int updateStatusByIdAndUpdatedAt(Order order);
    int deleteItemsByOrderId(@Param("orderId") String orderId);
    LocalDateTime selectUpdatedAtById(@Param("id") String id);
}
