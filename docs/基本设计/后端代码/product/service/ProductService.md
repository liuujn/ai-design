# ProductService

```java
package com.example.app.product.service;

import com.example.app.product.model.dto.request.*;
import com.example.app.product.model.dto.response.*;
import com.example.app.user.model.dto.response.PageResult;

public interface ProductService {
    PageResult<ProductListVO> list(ProductListQuery query);
    ProductDetailVO detail(String id);
    ProductCreateVO create(ProductCreateRequest request, String operatorId);
    ProductUpdateVO update(String id, ProductUpdateRequest request, String operatorId);
    void delete(String id, ProductDeleteRequest request, String operatorId);
    ProductStatusVO updateStatus(String id, ProductStatusRequest request, String operatorId);
}
```
