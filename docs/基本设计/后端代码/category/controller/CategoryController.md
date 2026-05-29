# CategoryController

```java
package com.example.app.category.controller;

import com.example.app.category.model.dto.request.*;
import com.example.app.category.model.dto.response.*;
import com.example.app.category.service.CategoryService;
import com.example.app.user.model.dto.response.PageResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<PageResult<CategoryListVO>> list(@Valid CategoryListQuery query) {
        return ResponseEntity.ok(categoryService.list(query));
    }

    @GetMapping("/all-active")
    public ResponseEntity<List<CategorySimpleVO>> allActive() {
        return ResponseEntity.ok(categoryService.findAllActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDetailVO> detail(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.detail(id));
    }

    @PostMapping
    public ResponseEntity<CategoryCreateVO> create(@Valid @RequestBody CategoryCreateRequest request) {
        String operatorId = "SYSTEM";
        return ResponseEntity.ok(categoryService.create(request, operatorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryUpdateVO> update(@PathVariable String id,
                                                    @Valid @RequestBody CategoryUpdateRequest request) {
        String operatorId = "SYSTEM";
        return ResponseEntity.ok(categoryService.update(id, request, operatorId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id,
                                        @Valid @RequestBody CategoryDeleteRequest request) {
        String operatorId = "SYSTEM";
        categoryService.delete(id, request, operatorId);
        return ResponseEntity.noContent().build();
    }
}
```
