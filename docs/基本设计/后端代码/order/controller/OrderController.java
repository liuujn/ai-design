package com.example.app.order.controller;

import com.example.app.order.model.dto.request.*;
import com.example.app.order.model.dto.response.*;
import com.example.app.order.service.OrderService;
import com.example.app.user.model.dto.response.PageResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<PageResult<OrderListVO>> list(@Valid OrderListQuery query) {
        return ResponseEntity.ok(orderService.list(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailVO> detail(@PathVariable String id) {
        return ResponseEntity.ok(orderService.detail(id));
    }

    @PostMapping
    public ResponseEntity<OrderCreateVO> create(@Valid @RequestBody OrderCreateRequest request) {
        String operatorId = "SYSTEM";
        return ResponseEntity.ok(orderService.create(request, operatorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderUpdateVO> update(@PathVariable String id,
                                                 @Valid @RequestBody OrderUpdateRequest request) {
        String operatorId = "SYSTEM";
        return ResponseEntity.ok(orderService.update(id, request, operatorId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id,
                                       @Valid @RequestBody OrderDeleteRequest request) {
        String operatorId = "SYSTEM";
        orderService.delete(id, request, operatorId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderStatusVO> updateStatus(@PathVariable String id,
                                                       @Valid @RequestBody OrderStatusRequest request) {
        String operatorId = "SYSTEM";
        return ResponseEntity.ok(orderService.updateStatus(id, request, operatorId));
    }
}
