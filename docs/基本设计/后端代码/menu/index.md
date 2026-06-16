# 菜单管理 后端コード

## パッケージ構造

```
com.example.app.menu/
├── controller/
│   └── MenuController.java
├── service/
│   ├── MenuService.java
│   └── impl/
│       └── MenuServiceImpl.java
├── mapper/
│   ├── MenuMapper.java
│   └── resources/
│       └── mapper/
│           └── MenuMapper.xml
└── model/
    ├── entity/
    │   └── Menu.java
    └── dto/
        ├── request/
        │   ├── MenuCreateRequest.java
        │   ├── MenuUpdateRequest.java
        │   └── MenuListQuery.java
        └── response/
            └── MenuVO.java
```

## ファイル一覧

### Controller
- [MenuController.java](controller/MenuController.java) - [MenuController.md](controller/MenuController.md)

### Service
- [MenuService.java](service/MenuService.java) - [MenuService.md](service/MenuService.md)
- [MenuServiceImpl.java](service/impl/MenuServiceImpl.java) - [MenuServiceImpl.md](service/impl/MenuServiceImpl.md)

### Mapper
- [MenuMapper.java](mapper/MenuMapper.java) - [MenuMapper.md](mapper/MenuMapper.md)
- [MenuMapper.xml](mapper/resources/mapper/MenuMapper.xml) - [MenuMapperXML.md](mapper/resources/mapper/MenuMapperXML.md)

### Model
- [Menu.java](model/entity/Menu.java) - [Menu.md](model/entity/Menu.md)

### DTO - Request
- [MenuCreateRequest.java](model/dto/request/MenuCreateRequest.java) - [MenuCreateRequest.md](model/dto/request/MenuCreateRequest.md)
- [MenuUpdateRequest.java](model/dto/request/MenuUpdateRequest.java) - [MenuUpdateRequest.md](model/dto/request/MenuUpdateRequest.md)
- [MenuListQuery.java](model/dto/request/MenuListQuery.java) - [MenuListQuery.md](model/dto/request/MenuListQuery.md)

### DTO - Response
- [MenuVO.java](model/dto/response/MenuVO.java) - [MenuVO.md](model/dto/response/MenuVO.md)
