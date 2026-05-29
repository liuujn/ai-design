# ProductController

```java
package com.example.app.product.controller;

import com.example.app.product.model.dto.request.*;
import com.example.app.product.model.dto.response.*;
import com.example.app.product.service.ProductService;
import com.example.app.user.model.dto.response.PageResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<PageResult<ProductListVO>> list(@Valid ProductListQuery query) {
        return ResponseEntity.ok(productService.list(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailVO> detail(@PathVariable String id) {
        return ResponseEntity.ok(productService.detail(id));
    }

    @PostMapping
    public ResponseEntity<ProductCreateVO> create(@Valid @RequestBody ProductCreateRequest request) {
        String operatorId = "SYSTEM";
        return ResponseEntity.ok(productService.create(request, operatorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductUpdateVO> update(@PathVariable String id,
                                                   @Valid @RequestBody ProductUpdateRequest request) {
        String operatorId = "SYSTEM";
        return ResponseEntity.ok(productService.update(id, request, operatorId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id,
                                       @Valid @RequestBody ProductDeleteRequest request) {
        String operatorId = "SYSTEM";
        productService.delete(id, request, operatorId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ProductStatusVO> updateStatus(@PathVariable String id,
                                                         @Valid @RequestBody ProductStatusRequest request) {
        String operatorId = "SYSTEM";
        return ResponseEntity.ok(productService.updateStatus(id, request, operatorId));
    }
}
```
