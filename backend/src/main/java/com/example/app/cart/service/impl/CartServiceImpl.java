package com.example.app.cart.service.impl;

import com.example.app.address.mapper.AddressMapper;
import com.example.app.address.model.entity.Address;
import com.example.app.cart.mapper.CartMapper;
import com.example.app.cart.model.dto.request.*;
import com.example.app.cart.model.dto.response.*;
import com.example.app.cart.model.entity.Cart;
import com.example.app.cart.model.entity.CartItem;
import com.example.app.cart.service.CartService;
import com.example.app.common.exception.BusinessException;
import com.example.app.order.mapper.OrderMapper;
import com.example.app.order.model.entity.Order;
import com.example.app.order.model.entity.OrderItem;
import com.example.app.product.mapper.ProductMapper;
import com.example.app.product.model.entity.Product;
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
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;
    private final ProductMapper productMapper;
    private final AddressMapper addressMapper;
    private final OrderMapper orderMapper;

    @Override
    @Transactional(readOnly = true)
    public CartVO getMyCart(String userId) {
        Cart cart = cartMapper.selectActiveByUserId(userId);
        if (cart == null) {
            CartVO empty = new CartVO();
            empty.setStatus("active");
            empty.setTotalAmount("0");
            empty.setItems(List.of());
            return empty;
        }
        return toCartVO(cart);
    }

    @Override
    public CartAddItemVO addItem(String userId, CartAddItemRequest request) {
        Product product = productMapper.selectById(request.getProductId());
        if (product == null) {
            throw new BusinessException("E9404", "商品不存在。");
        }
        if (product.getStock() < request.getQuantity()) {
            throw new BusinessException("E9403", "库存不足：" + product.getName() + " 当前库存 " + product.getStock() + "。");
        }

        Cart cart = cartMapper.selectActiveByUserId(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setId(UUID.randomUUID().toString());
            cart.setUserId(userId);
            cart.setStatus("active");
            cart.setTotalAmount(BigDecimal.ZERO);
            cart.setCreatedBy(userId);
            cartMapper.insert(cart);
        }

        CartItem existing = cartMapper.selectItemByCartIdAndProductId(cart.getId(), request.getProductId());
        if (existing != null) {
            int newQty = existing.getQuantity() + request.getQuantity();
            if (product.getStock() < newQty) {
                throw new BusinessException("E9403", "库存不足：" + product.getName() + " 当前库存 " + product.getStock() + "。");
            }
            LocalDateTime itemUpdatedAt = cartMapper.selectItemUpdatedAtById(existing.getId());
            cartMapper.updateItemQuantity(existing.getId(), newQty, itemUpdatedAt);
            recalcCartTotal(cart, userId);

            CartAddItemVO vo = new CartAddItemVO();
            vo.setId(existing.getId());
            vo.setProductId(product.getId());
            vo.setProductName(product.getName());
            vo.setQuantity(newQty);
            vo.setUnitPrice(product.getPrice() != null ? product.getPrice().toString() : "0");
            vo.setSubtotal(product.getPrice() != null ? product.getPrice().multiply(BigDecimal.valueOf(newQty)).toString() : "0");
            return vo;
        }

        CartItem item = new CartItem();
        item.setId(UUID.randomUUID().toString());
        item.setCartId(cart.getId());
        item.setProductId(request.getProductId());
        item.setProductName(product.getName());
        item.setQuantity(request.getQuantity());
        item.setUnitPrice(product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO);
        item.setCreatedBy(userId);
        cartMapper.insertItem(item);

        recalcCartTotal(cart, userId);

        CartAddItemVO vo = new CartAddItemVO();
        vo.setId(item.getId());
        vo.setProductId(product.getId());
        vo.setProductName(product.getName());
        vo.setQuantity(request.getQuantity());
        vo.setUnitPrice(item.getUnitPrice().toString());
        vo.setSubtotal(item.getUnitPrice().multiply(BigDecimal.valueOf(request.getQuantity())).toString());
        return vo;
    }

    @Override
    public CartItemUpdateVO updateItemQuantity(String userId, String itemId, CartItemQuantityRequest request) {
        CartItem item = cartMapper.selectItemById(itemId);
        if (item == null) {
            throw new BusinessException("E9404", "购物车明细不存在。");
        }

        Cart cart = cartMapper.selectById(item.getCartId());
        if (cart == null || !cart.getUserId().equals(userId)) {
            throw new BusinessException("E9404", "购物车不存在。");
        }

        LocalDateTime currentUpdatedAt = cartMapper.selectItemUpdatedAtById(itemId);
        String currentUpdatedAtStr = currentUpdatedAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        if (!Objects.equals(currentUpdatedAtStr, request.getUpdatedAt())) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        Product product = productMapper.selectById(item.getProductId());
        if (product != null && product.getStock() < request.getQuantity()) {
            throw new BusinessException("E9403", "库存不足：" + product.getName() + " 当前库存 " + product.getStock() + "。");
        }

        cartMapper.updateItemQuantity(itemId, request.getQuantity(), currentUpdatedAt);
        recalcCartTotal(cart, userId);

        CartItem updated = cartMapper.selectItemById(itemId);
        BigDecimal unitPrice = updated.getUnitPrice() != null ? updated.getUnitPrice() : BigDecimal.ZERO;
        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(updated.getQuantity()));

        CartItemUpdateVO vo = new CartItemUpdateVO();
        vo.setId(updated.getId());
        vo.setProductId(updated.getProductId());
        vo.setProductName(updated.getProductName());
        vo.setQuantity(updated.getQuantity());
        vo.setUnitPrice(unitPrice.toString());
        vo.setSubtotal(subtotal.toString());
        vo.setUpdatedAt(updated.getUpdatedAt() != null ? updated.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        return vo;
    }

    @Override
    public void deleteItem(String userId, String itemId, CartItemDeleteRequest request) {
        CartItem item = cartMapper.selectItemById(itemId);
        if (item == null) {
            throw new BusinessException("E9404", "购物车明细不存在。");
        }

        Cart cart = cartMapper.selectById(item.getCartId());
        if (cart == null || !cart.getUserId().equals(userId)) {
            throw new BusinessException("E9404", "购物车不存在。");
        }

        LocalDateTime currentUpdatedAt = cartMapper.selectItemUpdatedAtById(itemId);
        String currentUpdatedAtStr = currentUpdatedAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        if (!Objects.equals(currentUpdatedAtStr, request.getUpdatedAt())) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        int affected = cartMapper.deleteItemByIdAndUpdatedAt(itemId, currentUpdatedAt);
        if (affected == 0) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        recalcCartTotal(cart, userId);
    }

    @Override
    public CartAddressVO selectAddress(String userId, CartAddressRequest request) {
        Cart cart = cartMapper.selectActiveByUserId(userId);
        if (cart == null) {
            throw new BusinessException("E9404", "购物车不存在。");
        }

        Address address = addressMapper.selectById(request.getAddressId());
        if (address == null) {
            throw new BusinessException("E9404", "地址不存在。");
        }
        if (!address.getUserId().equals(userId)) {
            throw new BusinessException("E9404", "地址不存在。");
        }

        LocalDateTime currentUpdatedAt = cartMapper.selectUpdatedAtById(cart.getId());
        String currentUpdatedAtStr = currentUpdatedAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        if (!Objects.equals(currentUpdatedAtStr, request.getUpdatedAt())) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        cart.setAddressId(request.getAddressId());
        cart.setUpdatedAt(currentUpdatedAt);
        cart.setUpdatedBy(userId);
        int affected = cartMapper.updateByIdAndUpdatedAt(cart);
        if (affected == 0) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        Cart updated = cartMapper.selectById(cart.getId());
        CartAddressVO vo = new CartAddressVO();
        vo.setId(updated.getId());
        vo.setAddressId(updated.getAddressId());
        vo.setUpdatedAt(updated.getUpdatedAt() != null ? updated.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        return vo;
    }

    @Override
    public CartCheckoutVO checkout(String userId, CartCheckoutRequest request) {
        Cart cart = cartMapper.selectActiveByUserId(userId);
        if (cart == null) {
            throw new BusinessException("E9401", "购物车为空，请先添加商品。");
        }

        List<CartItem> items = cartMapper.selectItemsByCartId(cart.getId());
        if (items == null || items.isEmpty()) {
            throw new BusinessException("E9401", "购物车为空，请先添加商品。");
        }

        if (cart.getAddressId() == null || cart.getAddressId().isEmpty()) {
            throw new BusinessException("E9402", "请先选择配送地址。");
        }

        LocalDateTime currentUpdatedAt = cartMapper.selectUpdatedAtById(cart.getId());
        String currentUpdatedAtStr = currentUpdatedAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        if (!Objects.equals(currentUpdatedAtStr, request.getUpdatedAt())) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        // Verify stock for all items
        for (CartItem item : items) {
            Product product = productMapper.selectById(item.getProductId());
            if (product == null || product.getStock() < item.getQuantity()) {
                throw new BusinessException("E9403", "库存不足：" + item.getProductName() + " 当前库存 " + (product != null ? product.getStock() : 0) + "。");
            }
        }

        // Create order
        String orderId = UUID.randomUUID().toString();
        String orderNo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + orderId.substring(0, 4).toUpperCase();

        BigDecimal totalAmount = items.stream()
                .map(i -> i.getUnitPrice() != null ? i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())) : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setId(orderId);
        order.setOrderNo(orderNo);
        order.setCustomerName(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus("pending");
        order.setRemark(request.getRemark());
        order.setCreatedBy(userId);
        orderMapper.insert(order);

        // Create order items (batch insert)
        List<OrderItem> orderItems = items.stream().map(item -> {
            OrderItem oi = new OrderItem();
            oi.setId(UUID.randomUUID().toString());
            oi.setOrderId(orderId);
            oi.setItemName(item.getProductName());
            oi.setQuantity(item.getQuantity());
            oi.setUnitPrice(item.getUnitPrice());
            oi.setSubtotal(item.getUnitPrice() != null ? item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())) : BigDecimal.ZERO);
            return oi;
        }).collect(Collectors.toList());
        orderMapper.insertItems(orderItems);

        // Mark cart as checked_out
        cart.setStatus("checked_out");
        cart.setUpdatedAt(currentUpdatedAt);
        cart.setUpdatedBy(userId);
        cartMapper.updateByIdAndUpdatedAt(cart);

        CartCheckoutVO vo = new CartCheckoutVO();
        vo.setOrderId(orderId);
        vo.setOrderNo(orderNo);
        vo.setTotalAmount(totalAmount.toString());
        vo.setStatus("pending");
        vo.setMessage("下单成功");
        return vo;
    }

    @Override
    public void clearCart(String userId, CartDeleteRequest request) {
        Cart cart = cartMapper.selectActiveByUserId(userId);
        if (cart == null) return;

        LocalDateTime currentUpdatedAt = cartMapper.selectUpdatedAtById(cart.getId());
        String currentUpdatedAtStr = currentUpdatedAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        if (!Objects.equals(currentUpdatedAtStr, request.getUpdatedAt())) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        // Delete all items
        cartMapper.clearCartItems(cart.getId(), currentUpdatedAt, userId);

        // Mark cart as deleted
        cart.setStatus("checked_out");
        cart.setUpdatedAt(currentUpdatedAt);
        cart.setUpdatedBy(userId);
        cartMapper.updateByIdAndUpdatedAt(cart);
    }

    private void recalcCartTotal(Cart cart, String userId) {
        List<CartItem> items = cartMapper.selectItemsByCartId(cart.getId());
        BigDecimal total = items.stream()
                .map(i -> i.getUnitPrice() != null ? i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())) : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LocalDateTime cartUpdatedAt = cartMapper.selectUpdatedAtById(cart.getId());
        cart.setTotalAmount(total);
        cart.setUpdatedAt(cartUpdatedAt);
        cart.setUpdatedBy(userId);
        cartMapper.updateByIdAndUpdatedAt(cart);
    }

    private CartVO toCartVO(Cart cart) {
        CartVO vo = new CartVO();
        vo.setId(cart.getId());
        vo.setUserId(cart.getUserId());
        vo.setAddressId(cart.getAddressId());
        vo.setStatus(cart.getStatus());
        vo.setTotalAmount(cart.getTotalAmount() != null ? cart.getTotalAmount().toString() : "0");
        vo.setRemark(cart.getRemark());
        vo.setCreatedAt(cart.getCreatedAt() != null ? cart.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        vo.setUpdatedAt(cart.getUpdatedAt() != null ? cart.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);

        List<CartItem> items = cartMapper.selectItemsByCartId(cart.getId());
        if (items != null) {
            vo.setItems(items.stream().map(this::toItemVO).collect(Collectors.toList()));
        }

        if (cart.getAddressId() != null) {
            Address addr = addressMapper.selectById(cart.getAddressId());
            if (addr != null) {
                CartVO.AddressVO addrVO = new CartVO.AddressVO();
                addrVO.setId(addr.getId());
                addrVO.setRecipientName(addr.getRecipientName());
                addrVO.setRecipientPhone(addr.getRecipientPhone());
                addrVO.setProvince(addr.getProvince());
                addrVO.setCity(addr.getCity());
                addrVO.setDistrict(addr.getDistrict());
                addrVO.setStreet(addr.getStreet());
                vo.setAddress(addrVO);
            }
        }

        return vo;
    }

    private CartVO.CartItemVO toItemVO(CartItem item) {
        CartVO.CartItemVO vo = new CartVO.CartItemVO();
        vo.setId(item.getId());
        vo.setProductId(item.getProductId());
        vo.setProductName(item.getProductName());
        vo.setQuantity(item.getQuantity());
        BigDecimal unitPrice = item.getUnitPrice() != null ? item.getUnitPrice() : BigDecimal.ZERO;
        vo.setUnitPrice(unitPrice.toString());
        vo.setSubtotal(unitPrice.multiply(BigDecimal.valueOf(item.getQuantity())).toString());
        vo.setCreatedAt(item.getCreatedAt() != null ? item.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        vo.setUpdatedAt(item.getUpdatedAt() != null ? item.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        return vo;
    }
}
