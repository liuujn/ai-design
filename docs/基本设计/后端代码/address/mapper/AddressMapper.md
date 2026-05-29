# AddressMapper

```java
package com.example.app.address.mapper;

import com.example.app.address.model.dto.request.AddressListQuery;
import com.example.app.address.model.entity.Address;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AddressMapper {

    List<Address> selectPage(AddressListQuery query);

    Long count(AddressListQuery query);

    Address selectById(@Param("id") String id);

    List<Address> selectByUserId(@Param("userId") String userId);

    Long countByUserId(@Param("userId") String userId);

    int insert(Address address);

    int updateByIdAndUpdatedAt(Address address);

    int logicDeleteByIdAndUpdatedAt(@Param("id") String id, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);

    int updateIsDefaultByUserId(@Param("userId") String userId, @Param("isDefault") Boolean isDefault);

    int updateIsDefaultById(@Param("id") String id, @Param("isDefault") Boolean isDefault, @Param("updatedBy") String updatedBy);

    LocalDateTime selectUpdatedAtById(@Param("id") String id);
}
```
