package com.example.app.category.service.impl;

import com.example.app.category.mapper.CategoryMapper;
import com.example.app.category.model.dto.request.*;
import com.example.app.category.model.dto.response.*;
import com.example.app.category.model.entity.Category;
import com.example.app.category.service.CategoryService;
import com.example.app.common.exception.BusinessException;
import com.example.app.user.model.dto.response.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResult<CategoryListVO> list(CategoryListQuery query) {
        if (query.getPage() < 1) query.setPage(1);
        if (query.getSize() < 1 || query.getSize() > 100) query.setSize(20);

        long total = categoryMapper.count(query);
        List<Category> categories = categoryMapper.selectPage(query);

        List<CategoryListVO> content = categories.stream().map(c -> {
            CategoryListVO vo = new CategoryListVO();
            vo.setId(c.getId());
            vo.setName(c.getName());
            vo.setDescription(c.getDescription());
            vo.setSortOrder(c.getSortOrder());
            vo.setStatus(c.getStatus());
            vo.setCreatedAt(c.getCreatedAt() != null ? c.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
            vo.setUpdatedAt(c.getUpdatedAt() != null ? c.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
            return vo;
        }).collect(Collectors.toList());

        int totalPages = (int) Math.ceil((double) total / query.getSize());
        return new PageResult<>(content, query.getPage(), query.getSize(), total, totalPages);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDetailVO detail(String id) {
        if (id == null || id.isEmpty()) {
            throw new BusinessException("E9101", "分类ID不能为空。");
        }

        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException("E9404", "分类不存在。");
        }

        CategoryDetailVO vo = new CategoryDetailVO();
        vo.setId(category.getId());
        vo.setName(category.getName());
        vo.setDescription(category.getDescription());
        vo.setSortOrder(category.getSortOrder());
        vo.setStatus(category.getStatus());
        vo.setCreatedAt(category.getCreatedAt() != null ? category.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        vo.setUpdatedAt(category.getUpdatedAt() != null ? category.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        return vo;
    }

    @Override
    public CategoryCreateVO create(CategoryCreateRequest request, String operatorId) {
        Category category = new Category();
        category.setId(UUID.randomUUID().toString());
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        category.setStatus("active");
        category.setCreatedBy(operatorId);

        categoryMapper.insert(category);

        CategoryCreateVO vo = new CategoryCreateVO();
        vo.setId(category.getId());
        vo.setName(category.getName());
        vo.setDescription(category.getDescription());
        vo.setSortOrder(category.getSortOrder());
        vo.setCreatedAt(category.getCreatedAt() != null ? category.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        return vo;
    }

    @Override
    public CategoryUpdateVO update(String id, CategoryUpdateRequest request, String operatorId) {
        Category existing = categoryMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("E9404", "分类不存在。");
        }

        LocalDateTime currentUpdatedAt = categoryMapper.selectUpdatedAtById(id);
        String currentUpdatedAtStr = currentUpdatedAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        if (!Objects.equals(currentUpdatedAtStr, request.getUpdatedAt())) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        Category updateCategory = new Category();
        updateCategory.setId(id);
        updateCategory.setName(request.getName());
        updateCategory.setDescription(request.getDescription());
        updateCategory.setSortOrder(request.getSortOrder());
        updateCategory.setStatus(request.getStatus());
        updateCategory.setUpdatedAt(currentUpdatedAt);
        updateCategory.setUpdatedBy(operatorId);

        int affected = categoryMapper.updateByIdAndUpdatedAt(updateCategory);
        if (affected == 0) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        Category updated = categoryMapper.selectById(id);
        CategoryUpdateVO vo = new CategoryUpdateVO();
        vo.setId(id);
        vo.setName(updated.getName());
        vo.setStatus(updated.getStatus());
        vo.setUpdatedAt(updated.getUpdatedAt() != null ? updated.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        return vo;
    }

    @Override
    public void delete(String id, CategoryDeleteRequest request, String operatorId) {
        Category existing = categoryMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("E9404", "分类不存在。");
        }

        LocalDateTime currentUpdatedAt = categoryMapper.selectUpdatedAtById(id);
        String currentUpdatedAtStr = currentUpdatedAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        if (!Objects.equals(currentUpdatedAtStr, request.getUpdatedAt())) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        int affected = categoryMapper.logicDeleteByIdAndUpdatedAt(id, currentUpdatedAt, operatorId);
        if (affected == 0) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategorySimpleVO> findAllActive() {
        var categories = categoryMapper.selectAllActive();
        if (categories == null) return new ArrayList<>();
        return categories.stream().map(c -> {
            CategorySimpleVO vo = new CategorySimpleVO();
            vo.setId(c.getId());
            vo.setName(c.getName());
            return vo;
        }).collect(Collectors.toList());
    }
}
