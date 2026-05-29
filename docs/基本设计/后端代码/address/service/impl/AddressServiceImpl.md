# AddressServiceImpl

```java
package com.example.app.address.service.impl;

import com.example.app.address.mapper.AddressMapper;
import com.example.app.address.model.dto.request.*;
import com.example.app.address.model.dto.response.*;
import com.example.app.address.model.entity.Address;
import com.example.app.address.model.enums.AddressType;
import com.example.app.address.service.AddressService;
import com.example.app.common.exception.BusinessException;
import com.example.app.user.model.dto.response.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AddressServiceImpl implements AddressService {

    private final AddressMapper addressMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResult<AddressListVO> list(AddressListQuery query) {
        if (query.getPage() < 1) query.setPage(1);
        if (query.getSize() < 1 || query.getSize() > 100) query.setSize(20);

        if (query.getAddressType() != null && !query.getAddressType().isEmpty()) {
            if (!AddressType.isValid(query.getAddressType())) {
                throw new BusinessException("E9903", "请输入有效的地址类型值。");
            }
        }

        long total = addressMapper.count(query);
        List<Address> addresses = addressMapper.selectPage(query);

        List<AddressListVO> content = addresses.stream().map(a -> {
            AddressListVO vo = new AddressListVO();
            vo.setId(a.getId());
            vo.setUserId(a.getUserId());
            vo.setAddressType(a.getAddressType());
            vo.setRecipientName(a.getRecipientName());
            vo.setRecipientPhone(a.getRecipientPhone());
            vo.setFullAddress(buildFullAddress(a));
            vo.setIsDefault(a.getIsDefault());
            vo.setCreatedAt(a.getCreatedAt() != null ? a.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
            vo.setUpdatedAt(a.getUpdatedAt() != null ? a.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
            return vo;
        }).collect(Collectors.toList());

        int totalPages = (int) Math.ceil((double) total / query.getSize());
        return new PageResult<>(content, query.getPage(), query.getSize(), total, totalPages);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressDetailVO detail(String id) {
        if (id == null || id.isEmpty()) {
            throw new BusinessException("E9101", "地址ID不能为空。");
        }

        Address address = addressMapper.selectById(id);
        if (address == null) {
            throw new BusinessException("E9404", "地址不存在。");
        }

        AddressDetailVO vo = new AddressDetailVO();
        vo.setId(address.getId());
        vo.setUserId(address.getUserId());
        vo.setAddressType(address.getAddressType());
        vo.setRecipientName(address.getRecipientName());
        vo.setRecipientPhone(address.getRecipientPhone());
        vo.setCountry(address.getCountry());
        vo.setProvince(address.getProvince());
        vo.setCity(address.getCity());
        vo.setDistrict(address.getDistrict());
        vo.setStreet(address.getStreet());
        vo.setPostalCode(address.getPostalCode());
        vo.setIsDefault(address.getIsDefault());
        vo.setCreatedAt(address.getCreatedAt() != null ? address.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        vo.setUpdatedAt(address.getUpdatedAt() != null ? address.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        return vo;
    }

    @Override
    public AddressCreateVO create(AddressCreateRequest request, String operatorId) {
        if (!AddressType.isValid(request.getAddressType())) {
            throw new BusinessException("E9903", "请输入有效的地址类型值。");
        }

        long count = addressMapper.countByUserId(operatorId);
        if (count >= 20) {
            throw new BusinessException("E9413", "地址数量不能超过20条。");
        }

        boolean isDefault = request.getIsDefault() != null && request.getIsDefault();
        if (isDefault) {
            addressMapper.updateIsDefaultByUserId(operatorId, false);
        }

        Address address = new Address();
        address.setId(UUID.randomUUID().toString());
        address.setUserId(operatorId);
        address.setAddressType(request.getAddressType());
        address.setRecipientName(request.getRecipientName());
        address.setRecipientPhone(request.getRecipientPhone());
        address.setCountry(request.getCountry());
        address.setProvince(request.getProvince());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setStreet(request.getStreet());
        address.setPostalCode(request.getPostalCode());
        address.setIsDefault(isDefault);
        address.setCreatedBy(operatorId);

        addressMapper.insert(address);

        AddressCreateVO vo = new AddressCreateVO();
        vo.setId(address.getId());
        vo.setAddressType(address.getAddressType());
        vo.setRecipientName(address.getRecipientName());
        vo.setRecipientPhone(address.getRecipientPhone());
        vo.setIsDefault(address.getIsDefault());
        vo.setCreatedAt(address.getCreatedAt() != null ? address.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        return vo;
    }

    @Override
    public AddressUpdateVO update(String id, AddressUpdateRequest request, String operatorId) {
        Address existing = addressMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("E9404", "地址不存在。");
        }

        LocalDateTime currentUpdatedAt = addressMapper.selectUpdatedAtById(id);
        if (currentUpdatedAt == null) {
            throw new BusinessException("E9404", "地址不存在。");
        }

        String currentUpdatedAtStr = currentUpdatedAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        if (!Objects.equals(currentUpdatedAtStr, request.getUpdatedAt())) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        boolean isDefault = request.getIsDefault() != null && request.getIsDefault();
        if (isDefault && !Boolean.TRUE.equals(existing.getIsDefault())) {
            addressMapper.updateIsDefaultByUserId(existing.getUserId(), false);
        }

        Address updateAddress = new Address();
        updateAddress.setId(id);
        updateAddress.setAddressType(request.getAddressType());
        updateAddress.setRecipientName(request.getRecipientName());
        updateAddress.setRecipientPhone(request.getRecipientPhone());
        updateAddress.setCountry(request.getCountry());
        updateAddress.setProvince(request.getProvince());
        updateAddress.setCity(request.getCity());
        updateAddress.setDistrict(request.getDistrict());
        updateAddress.setStreet(request.getStreet());
        updateAddress.setPostalCode(request.getPostalCode());
        updateAddress.setIsDefault(isDefault);
        updateAddress.setUpdatedAt(currentUpdatedAt);
        updateAddress.setUpdatedBy(operatorId);

        int affected = addressMapper.updateByIdAndUpdatedAt(updateAddress);
        if (affected == 0) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        Address updated = addressMapper.selectById(id);
        AddressUpdateVO vo = new AddressUpdateVO();
        vo.setId(id);
        vo.setAddressType(updated.getAddressType());
        vo.setRecipientName(updated.getRecipientName());
        vo.setRecipientPhone(updated.getRecipientPhone());
        vo.setIsDefault(updated.getIsDefault());
        vo.setUpdatedAt(updated.getUpdatedAt() != null ? updated.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        return vo;
    }

    @Override
    public void delete(String id, AddressDeleteRequest request, String operatorId) {
        Address existing = addressMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("E9404", "地址不存在。");
        }

        LocalDateTime currentUpdatedAt = addressMapper.selectUpdatedAtById(id);
        String currentUpdatedAtStr = currentUpdatedAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        if (!Objects.equals(currentUpdatedAtStr, request.getUpdatedAt())) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        int affected = addressMapper.logicDeleteByIdAndUpdatedAt(id, currentUpdatedAt, operatorId);
        if (affected == 0) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }
    }

    @Override
    public AddressDefaultVO setDefault(String id, AddressDefaultRequest request, String operatorId) {
        Address existing = addressMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("E9404", "地址不存在。");
        }

        LocalDateTime currentUpdatedAt = addressMapper.selectUpdatedAtById(id);
        String currentUpdatedAtStr = currentUpdatedAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        if (!Objects.equals(currentUpdatedAtStr, request.getUpdatedAt())) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        addressMapper.updateIsDefaultByUserId(existing.getUserId(), false);
        addressMapper.updateIsDefaultById(id, true, operatorId);

        Address updated = addressMapper.selectById(id);
        AddressDefaultVO vo = new AddressDefaultVO();
        vo.setId(id);
        vo.setIsDefault(true);
        vo.setUpdatedAt(updated.getUpdatedAt() != null ? updated.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        return vo;
    }

    private String buildFullAddress(Address a) {
        StringBuilder sb = new StringBuilder();
        if (a.getProvince() != null) sb.append(a.getProvince());
        if (a.getCity() != null) sb.append(a.getCity());
        if (a.getDistrict() != null) sb.append(a.getDistrict());
        if (a.getStreet() != null) sb.append(a.getStreet());
        return sb.toString();
    }
}
```
