# CartMapper

```java
package com.example.app.cart.mapper;

import com.example.app.cart.model.entity.Cart;
import com.example.app.cart.model.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CartMapper {
    Cart selectActiveByUserId(@Param("userId") String userId);
    Cart selectById(@Param("id") String id);
    int insert(Cart cart);
    int updateByIdAndUpdatedAt(Cart cart);
    int clearCartItems(@Param("cartId") String cartId, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);
    LocalDateTime selectUpdatedAtById(@Param("id") String id);

    List<CartItem> selectItemsByCartId(@Param("cartId") String cartId);
    CartItem selectItemById(@Param("id") String id);
    CartItem selectItemByCartIdAndProductId(@Param("cartId") String cartId, @Param("productId") String productId);
    int insertItem(CartItem item);
    int updateItemQuantity(@Param("id") String id, @Param("quantity") Integer quantity, @Param("updatedAt") LocalDateTime updatedAt);
    int deleteItemByIdAndUpdatedAt(@Param("id") String id, @Param("updatedAt") LocalDateTime updatedAt);
    LocalDateTime selectItemUpdatedAtById(@Param("id") String id);
}
```
