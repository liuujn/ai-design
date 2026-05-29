# AddressController

```java
package com.example.app.address.controller;

import com.example.app.address.model.dto.request.*;
import com.example.app.address.model.dto.response.*;
import com.example.app.address.service.AddressService;
import com.example.app.user.model.dto.response.PageResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<PageResult<AddressListVO>> list(@Valid AddressListQuery query) {
        return ResponseEntity.ok(addressService.list(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDetailVO> detail(@PathVariable String id) {
        return ResponseEntity.ok(addressService.detail(id));
    }

    @PostMapping
    public ResponseEntity<AddressCreateVO> create(@Valid @RequestBody AddressCreateRequest request) {
        String operatorId = "SYSTEM";
        return ResponseEntity.ok(addressService.create(request, operatorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressUpdateVO> update(@PathVariable String id,
                                                   @Valid @RequestBody AddressUpdateRequest request) {
        String operatorId = "SYSTEM";
        return ResponseEntity.ok(addressService.update(id, request, operatorId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id,
                                        @Valid @RequestBody AddressDeleteRequest request) {
        String operatorId = "SYSTEM";
        addressService.delete(id, request, operatorId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/default")
    public ResponseEntity<AddressDefaultVO> setDefault(@PathVariable String id,
                                                        @Valid @RequestBody AddressDefaultRequest request) {
        String operatorId = "SYSTEM";
        return ResponseEntity.ok(addressService.setDefault(id, request, operatorId));
    }
}
```
