# CartController

```java
package com.example.app.cart.controller;

import com.example.app.cart.model.dto.request.*;
import com.example.app.cart.model.dto.response.*;
import com.example.app.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/my")
    public ResponseEntity<CartVO> getMyCart() {
        String userId = "SYSTEM";
        return ResponseEntity.ok(cartService.getMyCart(userId));
    }

    @PostMapping("/items")
    public ResponseEntity<CartAddItemVO> addItem(@Valid @RequestBody CartAddItemRequest request) {
        String userId = "SYSTEM";
        return ResponseEntity.ok(cartService.addItem(userId, request));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartItemUpdateVO> updateItemQuantity(@PathVariable String itemId,
                                                                @Valid @RequestBody CartItemQuantityRequest request) {
        String userId = "SYSTEM";
        return ResponseEntity.ok(cartService.updateItemQuantity(userId, itemId, request));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable String itemId,
                                           @Valid @RequestBody CartItemDeleteRequest request) {
        String userId = "SYSTEM";
        cartService.deleteItem(userId, itemId, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/address")
    public ResponseEntity<CartAddressVO> selectAddress(@Valid @RequestBody CartAddressRequest request) {
        String userId = "SYSTEM";
        return ResponseEntity.ok(cartService.selectAddress(userId, request));
    }

    @PostMapping("/checkout")
    public ResponseEntity<CartCheckoutVO> checkout(@Valid @RequestBody CartCheckoutRequest request) {
        String userId = "SYSTEM";
        return ResponseEntity.ok(cartService.checkout(userId, request));
    }

    @DeleteMapping("/my")
    public ResponseEntity<Void> clearCart(@Valid @RequestBody CartDeleteRequest request) {
        String userId = "SYSTEM";
        cartService.clearCart(userId, request);
        return ResponseEntity.noContent().build();
    }
}
```
