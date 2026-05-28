# UserMapper

```java
package com.example.user.mapper;

import com.example.user.model.dto.request.UserListQuery;
import com.example.user.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserMapper {

    List<User> selectPage(UserListQuery query);

    Long count(UserListQuery query);

    User selectById(@Param("id") String id);

    User selectByUsername(@Param("username") String username);

    User selectByEmail(@Param("email") String email);

    User selectByEmailExcludeId(@Param("email") String email, @Param("excludeId") String excludeId);

    LocalDateTime selectUpdatedAtById(@Param("id") String id);

    int insert(User user);

    int updateByIdAndUpdatedAt(User user);

    int logicDeleteByIdAndUpdatedAt(@Param("id") String id, @Param("updatedAt") LocalDateTime updatedAt);

    int updateStatusByIdAndUpdatedAt(User user);
}
```
