package com.example.app.cart.service;

import com.example.app.cart.model.dto.request.*;
import com.example.app.cart.model.dto.response.*;

public interface CartService {
    CartVO getMyCart(String userId);
    CartAddItemVO addItem(String userId, CartAddItemRequest request);
    CartItemUpdateVO updateItemQuantity(String userId, String itemId, CartItemQuantityRequest request);
    void deleteItem(String userId, String itemId, CartItemDeleteRequest request);
    CartAddressVO selectAddress(String userId, CartAddressRequest request);
    CartCheckoutVO checkout(String userId, CartCheckoutRequest request);
    void clearCart(String userId, CartDeleteRequest request);
}
