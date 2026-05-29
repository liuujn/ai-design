package com.example.app.product.service.impl;

import com.example.app.category.mapper.CategoryMapper;
import com.example.app.product.mapper.ProductMapper;
import com.example.app.product.model.dto.request.*;
import com.example.app.product.model.dto.response.*;
import com.example.app.product.model.entity.Product;
import com.example.app.product.model.enums.ProductStatus;
import com.example.app.product.service.ProductService;
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
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResult<ProductListVO> list(ProductListQuery query) {
        if (query.getPage() < 1) query.setPage(1);
        if (query.getSize() < 1 || query.getSize() > 100) query.setSize(20);

        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            if (!ProductStatus.isValid(query.getStatus())) {
                throw new BusinessException("E9903", "请输入有效的商品状态值。");
            }
        }

        long total = productMapper.count(query);
        List<Product> products = productMapper.selectPage(query);

        List<ProductListVO> content = products.stream().map(p -> {
            ProductListVO vo = new ProductListVO();
            vo.setId(p.getId());
            vo.setName(p.getName());
            vo.setPrice(p.getPrice());
            vo.setStock(p.getStock());
            vo.setCategoryId(p.getCategoryId());
            vo.setCategory(p.getCategory());
            vo.setStatus(p.getStatus());
            vo.setCreatedAt(p.getCreatedAt() != null ? p.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
            vo.setUpdatedAt(p.getUpdatedAt() != null ? p.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
            return vo;
        }).collect(Collectors.toList());

        int totalPages = (int) Math.ceil((double) total / query.getSize());
        return new PageResult<>(content, query.getPage(), query.getSize(), total, totalPages);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailVO detail(String id) {
        if (id == null || id.isEmpty()) {
            throw new BusinessException("E9101", "商品ID不能为空。");
        }

        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException("E9404", "商品不存在。");
        }

        ProductDetailVO vo = new ProductDetailVO();
        vo.setId(product.getId());
        vo.setName(product.getName());
        vo.setDescription(product.getDescription());
        vo.setPrice(product.getPrice());
        vo.setStock(product.getStock());
        vo.setCategoryId(product.getCategoryId());
        vo.setCategory(product.getCategory());
        vo.setImageUrl(product.getImageUrl());
        vo.setStatus(product.getStatus());
        vo.setCreatedAt(product.getCreatedAt() != null ? product.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        vo.setUpdatedAt(product.getUpdatedAt() != null ? product.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        return vo;
    }

    @Override
    public ProductCreateVO create(ProductCreateRequest request, String operatorId) {
        if (request.getStatus() != null && !ProductStatus.isValid(request.getStatus())) {
            throw new BusinessException("E9903", "请输入有效的商品状态值。");
        }

        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock() != null ? request.getStock() : 0);
        if (request.getCategoryId() != null) {
            var cat = categoryMapper.selectById(request.getCategoryId());
            if (cat != null) {
                product.setCategoryId(request.getCategoryId());
                product.setCategory(cat.getName());
            }
        } else {
            product.setCategory(request.getCategory());
        }
        product.setImageUrl(request.getImageUrl());
        product.setStatus(request.getStatus() != null ? request.getStatus() : "active");
        product.setCreatedBy(operatorId);

        productMapper.insert(product);

        ProductCreateVO vo = new ProductCreateVO();
        vo.setId(product.getId());
        vo.setName(product.getName());
        vo.setPrice(product.getPrice());
        vo.setStock(product.getStock());
        vo.setCategoryId(product.getCategoryId());
        vo.setStatus(product.getStatus());
        vo.setCreatedAt(product.getCreatedAt() != null ? product.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        return vo;
    }

    @Override
    public ProductUpdateVO update(String id, ProductUpdateRequest request, String operatorId) {
        Product existing = productMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("E9404", "商品不存在。");
        }

        LocalDateTime currentUpdatedAt = productMapper.selectUpdatedAtById(id);
        String currentUpdatedAtStr = currentUpdatedAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        if (!Objects.equals(currentUpdatedAtStr, request.getUpdatedAt())) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        Product updateProduct = new Product();
        updateProduct.setId(id);
        updateProduct.setName(request.getName());
        updateProduct.setDescription(request.getDescription());
        updateProduct.setPrice(request.getPrice());
        updateProduct.setStock(request.getStock());
        if (request.getCategoryId() != null) {
            var cat = categoryMapper.selectById(request.getCategoryId());
            if (cat != null) {
                updateProduct.setCategoryId(request.getCategoryId());
                updateProduct.setCategory(cat.getName());
            }
        } else {
            updateProduct.setCategory(request.getCategory());
        }
        updateProduct.setImageUrl(request.getImageUrl());
        updateProduct.setStatus(request.getStatus());
        updateProduct.setUpdatedAt(currentUpdatedAt);
        updateProduct.setUpdatedBy(operatorId);

        int affected = productMapper.updateByIdAndUpdatedAt(updateProduct);
        if (affected == 0) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        Product updated = productMapper.selectById(id);
        ProductUpdateVO vo = new ProductUpdateVO();
        vo.setId(id);
        vo.setName(updated.getName());
        vo.setPrice(updated.getPrice());
        vo.setStock(updated.getStock());
        vo.setStatus(updated.getStatus());
        vo.setUpdatedAt(updated.getUpdatedAt() != null ? updated.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        return vo;
    }

    @Override
    public void delete(String id, ProductDeleteRequest request, String operatorId) {
        Product existing = productMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("E9404", "商品不存在。");
        }

        LocalDateTime currentUpdatedAt = productMapper.selectUpdatedAtById(id);
        String currentUpdatedAtStr = currentUpdatedAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        if (!Objects.equals(currentUpdatedAtStr, request.getUpdatedAt())) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        int affected = productMapper.logicDeleteByIdAndUpdatedAt(id, currentUpdatedAt, operatorId);
        if (affected == 0) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }
    }

    @Override
    public ProductStatusVO updateStatus(String id, ProductStatusRequest request, String operatorId) {
        Product existing = productMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("E9404", "商品不存在。");
        }

        if (!ProductStatus.isValid(request.getStatus())) {
            throw new BusinessException("E9903", "请输入有效的商品状态值。");
        }

        LocalDateTime currentUpdatedAt = productMapper.selectUpdatedAtById(id);
        String currentUpdatedAtStr = currentUpdatedAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        if (!Objects.equals(currentUpdatedAtStr, request.getUpdatedAt())) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        Product updateProduct = new Product();
        updateProduct.setId(id);
        updateProduct.setStatus(request.getStatus());
        updateProduct.setUpdatedAt(currentUpdatedAt);
        updateProduct.setUpdatedBy(operatorId);

        int affected = productMapper.updateStatusByIdAndUpdatedAt(updateProduct);
        if (affected == 0) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        Product updated = productMapper.selectById(id);
        ProductStatusVO vo = new ProductStatusVO();
        vo.setId(id);
        vo.setStatus(updated.getStatus());
        vo.setUpdatedAt(updated.getUpdatedAt() != null ? updated.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        return vo;
    }
}
