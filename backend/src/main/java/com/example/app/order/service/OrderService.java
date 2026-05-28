package com.example.app.order.service;

import com.example.app.order.model.dto.request.*;
import com.example.app.order.model.dto.response.*;
import com.example.app.user.model.dto.response.PageResult;

public interface OrderService {
    PageResult<OrderListVO> list(OrderListQuery query);
    OrderDetailVO detail(String id);
    OrderCreateVO create(OrderCreateRequest request, String operatorId);
    OrderUpdateVO update(String id, OrderUpdateRequest request, String operatorId);
    void delete(String id, OrderDeleteRequest request, String operatorId);
    OrderStatusVO updateStatus(String id, OrderStatusRequest request, String operatorId);
}
