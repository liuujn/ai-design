# CategoryService

```java
package com.example.app.category.service;

import com.example.app.category.model.dto.request.*;
import com.example.app.category.model.dto.response.*;
import com.example.app.user.model.dto.response.PageResult;
import java.util.List;

public interface CategoryService {
    PageResult<CategoryListVO> list(CategoryListQuery query);
    CategoryDetailVO detail(String id);
    CategoryCreateVO create(CategoryCreateRequest request, String operatorId);
    CategoryUpdateVO update(String id, CategoryUpdateRequest request, String operatorId);
    void delete(String id, CategoryDeleteRequest request, String operatorId);
    List<CategorySimpleVO> findAllActive();
}
```
