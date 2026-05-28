package com.example.app.address.service;

import com.example.app.address.model.dto.request.*;
import com.example.app.address.model.dto.response.*;
import com.example.app.user.model.dto.response.PageResult;

public interface AddressService {

    PageResult<AddressListVO> list(AddressListQuery query);

    AddressDetailVO detail(String id);

    AddressCreateVO create(AddressCreateRequest request, String operatorId);

    AddressUpdateVO update(String id, AddressUpdateRequest request, String operatorId);

    void delete(String id, AddressDeleteRequest request, String operatorId);

    AddressDefaultVO setDefault(String id, AddressDefaultRequest request, String operatorId);
}
