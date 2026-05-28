package com.example.app.order.service.impl;

import com.example.app.order.mapper.OrderMapper;
import com.example.app.order.model.dto.request.*;
import com.example.app.order.model.dto.response.*;
import com.example.app.order.model.entity.Order;
import com.example.app.order.model.entity.OrderItem;
import com.example.app.order.model.enums.OrderStatus;
import com.example.app.order.service.OrderService;
import com.example.app.common.exception.BusinessException;
import com.example.app.user.model.dto.response.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResult<OrderListVO> list(OrderListQuery query) {
        if (query.getPage() < 1) query.setPage(1);
        if (query.getSize() < 1 || query.getSize() > 100) query.setSize(20);

        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            if (!OrderStatus.isValid(query.getStatus())) {
                throw new BusinessException("E9903", "请输入有效的订单状态值。");
            }
        }

        long total = orderMapper.count(query);
        List<Order> orders = orderMapper.selectPage(query);

        List<OrderListVO> content = orders.stream().map(o -> {
            OrderListVO vo = new OrderListVO();
            vo.setId(o.getId());
            vo.setOrderNo(o.getOrderNo());
            vo.setCustomerName(o.getCustomerName());
            vo.setTotalAmount(o.getTotalAmount());
            vo.setStatus(o.getStatus());
            vo.setCreatedAt(o.getCreatedAt() != null ? o.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
            vo.setUpdatedAt(o.getUpdatedAt() != null ? o.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
            return vo;
        }).collect(Collectors.toList());

        int totalPages = (int) Math.ceil((double) total / query.getSize());
        return new PageResult<>(content, query.getPage(), query.getSize(), total, totalPages);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDetailVO detail(String id) {
        if (id == null || id.isEmpty()) {
            throw new BusinessException("E9101", "订单ID不能为空。");
        }

        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("E9404", "订单不存在。");
        }

        List<OrderItem> items = orderMapper.selectItemsByOrderId(id);

        OrderDetailVO vo = new OrderDetailVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setCustomerName(order.getCustomerName());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setStatus(order.getStatus());
        vo.setRemark(order.getRemark());
        vo.setCreatedAt(order.getCreatedAt() != null ? order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        vo.setUpdatedAt(order.getUpdatedAt() != null ? order.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        vo.setItems(items.stream().map(item -> {
            OrderDetailVO.OrderItemVO itemVO = new OrderDetailVO.OrderItemVO();
            itemVO.setId(item.getId());
            itemVO.setItemName(item.getItemName());
            itemVO.setQuantity(item.getQuantity());
            itemVO.setUnitPrice(item.getUnitPrice());
            itemVO.setSubtotal(item.getSubtotal());
            return itemVO;
        }).collect(Collectors.toList()));
        return vo;
    }

    @Override
    public OrderCreateVO create(OrderCreateRequest request, String operatorId) {
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setOrderNo(generateOrderNo());
        order.setCustomerName(request.getCustomerName());
        order.setRemark(request.getRemark());
        order.setStatus("pending");
        order.setCreatedBy(operatorId);

        BigDecimal total = BigDecimal.ZERO;
        for (OrderCreateRequest.OrderItemRequest itemReq : request.getItems()) {
            BigDecimal subtotal = itemReq.getUnitPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            total = total.add(subtotal);
        }
        order.setTotalAmount(total);

        orderMapper.insert(order);

        for (OrderCreateRequest.OrderItemRequest itemReq : request.getItems()) {
            OrderItem item = new OrderItem();
            item.setId(UUID.randomUUID().toString());
            item.setOrderId(order.getId());
            item.setItemName(itemReq.getItemName());
            item.setQuantity(itemReq.getQuantity());
            item.setUnitPrice(itemReq.getUnitPrice());
            item.setSubtotal(itemReq.getUnitPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity())));
            orderMapper.insertItems(List.of(item));
        }

        OrderCreateVO vo = new OrderCreateVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setStatus(order.getStatus());
        vo.setCreatedAt(order.getCreatedAt() != null ? order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        return vo;
    }

    @Override
    public OrderUpdateVO update(String id, OrderUpdateRequest request, String operatorId) {
        Order existing = orderMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("E9404", "订单不存在。");
        }

        LocalDateTime currentUpdatedAt = orderMapper.selectUpdatedAtById(id);
        String currentUpdatedAtStr = currentUpdatedAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        if (!Objects.equals(currentUpdatedAtStr, request.getUpdatedAt())) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        Order updateOrder = new Order();
        updateOrder.setId(id);
        updateOrder.setCustomerName(request.getCustomerName());
        updateOrder.setRemark(request.getRemark());
        updateOrder.setUpdatedAt(currentUpdatedAt);
        updateOrder.setUpdatedBy(operatorId);

        BigDecimal total = BigDecimal.ZERO;
        if (request.getItems() != null) {
            orderMapper.deleteItemsByOrderId(id);
            for (OrderUpdateRequest.OrderItemRequest itemReq : request.getItems()) {
                OrderItem item = new OrderItem();
                item.setId(UUID.randomUUID().toString());
                item.setOrderId(id);
                item.setItemName(itemReq.getItemName());
                item.setQuantity(itemReq.getQuantity());
                item.setUnitPrice(itemReq.getUnitPrice());
                item.setSubtotal(itemReq.getUnitPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity())));
                orderMapper.insertItems(List.of(item));
                total = total.add(item.getSubtotal());
            }
        }
        updateOrder.setTotalAmount(total);

        int affected = orderMapper.updateByIdAndUpdatedAt(updateOrder);
        if (affected == 0) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        Order updated = orderMapper.selectById(id);
        OrderUpdateVO vo = new OrderUpdateVO();
        vo.setId(id);
        vo.setOrderNo(updated.getOrderNo());
        vo.setTotalAmount(updated.getTotalAmount());
        vo.setStatus(updated.getStatus());
        vo.setUpdatedAt(updated.getUpdatedAt() != null ? updated.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        return vo;
    }

    @Override
    public void delete(String id, OrderDeleteRequest request, String operatorId) {
        Order existing = orderMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("E9404", "订单不存在。");
        }

        LocalDateTime currentUpdatedAt = orderMapper.selectUpdatedAtById(id);
        String currentUpdatedAtStr = currentUpdatedAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        if (!Objects.equals(currentUpdatedAtStr, request.getUpdatedAt())) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        int affected = orderMapper.logicDeleteByIdAndUpdatedAt(id, currentUpdatedAt, operatorId);
        if (affected == 0) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }
    }

    @Override
    public OrderStatusVO updateStatus(String id, OrderStatusRequest request, String operatorId) {
        Order existing = orderMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("E9404", "订单不存在。");
        }

        if (!OrderStatus.isValid(request.getStatus())) {
            throw new BusinessException("E9903", "请输入有效的订单状态值。");
        }

        LocalDateTime currentUpdatedAt = orderMapper.selectUpdatedAtById(id);
        String currentUpdatedAtStr = currentUpdatedAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        if (!Objects.equals(currentUpdatedAtStr, request.getUpdatedAt())) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        Order updateOrder = new Order();
        updateOrder.setId(id);
        updateOrder.setStatus(request.getStatus());
        updateOrder.setUpdatedAt(currentUpdatedAt);
        updateOrder.setUpdatedBy(operatorId);

        int affected = orderMapper.updateStatusByIdAndUpdatedAt(updateOrder);
        if (affected == 0) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        Order updated = orderMapper.selectById(id);
        OrderStatusVO vo = new OrderStatusVO();
        vo.setId(id);
        vo.setStatus(updated.getStatus());
        vo.setUpdatedAt(updated.getUpdatedAt() != null ? updated.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        return vo;
    }

    private String generateOrderNo() {
        return "ORD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
               + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
}
