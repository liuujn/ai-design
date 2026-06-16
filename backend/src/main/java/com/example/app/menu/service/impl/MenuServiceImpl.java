package com.example.app.menu.service.impl;

import com.example.app.menu.mapper.MenuMapper;
import com.example.app.menu.model.dto.request.MenuCreateRequest;
import com.example.app.menu.model.dto.request.MenuListQuery;
import com.example.app.menu.model.dto.request.MenuUpdateRequest;
import com.example.app.menu.model.dto.response.MenuVO;
import com.example.app.menu.model.entity.Menu;
import com.example.app.menu.service.MenuService;
import com.example.app.common.exception.BusinessException;
import com.example.app.permission.mapper.PermissionMapper;
import com.example.app.user.model.dto.response.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class MenuServiceImpl implements MenuService {

    private final MenuMapper menuMapper;
    private final PermissionMapper permissionMapper;

    private static final Map<String, String[][]> PAGE_ACTIONS = new HashMap<>();
    static {
        PAGE_ACTIONS.put("user.html", new String[][]{{"view","View"},{"create","Create"},{"edit","Edit"},{"delete","Delete"},{"toggle_status","Toggle Status"}});
        PAGE_ACTIONS.put("address.html", new String[][]{{"view","View"},{"create","Create"},{"edit","Edit"},{"delete","Delete"},{"set_default","Set Default"}});
        PAGE_ACTIONS.put("category.html", new String[][]{{"view","View"},{"create","Create"},{"edit","Edit"},{"delete","Delete"}});
        PAGE_ACTIONS.put("product.html", new String[][]{{"view","View"},{"create","Create"},{"edit","Edit"},{"delete","Delete"},{"toggle_status","Toggle Status"},{"add_to_cart","Add to Cart"}});
        PAGE_ACTIONS.put("order.html", new String[][]{{"view","View"},{"create","Create"},{"detail","Detail"},{"confirm","Confirm"},{"ship","Ship"},{"complete","Complete"},{"cancel","Cancel"},{"delete","Delete"},{"checkout","Checkout"}});
        PAGE_ACTIONS.put("cart.html", new String[][]{{"view","View"},{"delete","Delete"},{"checkout","Checkout"}});
        PAGE_ACTIONS.put("menu.html", new String[][]{{"view","View"},{"create","Create"},{"edit","Edit"},{"delete","Delete"},{"toggle_status","Toggle Status"}});
        PAGE_ACTIONS.put("permission.html", new String[][]{{"view","View"}});
    }

    private static final String[][] DEFAULT_ACTIONS = {
        {"view", "View"},
        {"create", "Create"},
        {"edit", "Edit"},
        {"delete", "Delete"}
    };

    @Override
    @Transactional(readOnly = true)
    public PageResult<MenuVO> list(MenuListQuery query) {
        if (query.getPage() < 1) query.setPage(1);
        if (query.getSize() < 1 || query.getSize() > 100) query.setSize(20);

        long total = menuMapper.count(query);
        List<Menu> menus = menuMapper.selectPage(query);

        List<MenuVO> content = menus.stream().map(this::toVO).collect(Collectors.toList());

        int totalPages = (int) Math.ceil((double) total / query.getSize());
        return new PageResult<>(content, query.getPage(), query.getSize(), total, totalPages);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuVO> sidebar() {
        List<Menu> menus = menuMapper.selectAllActiveOrdered();
        return menus.stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuVO> sidebar(String userId) {
        List<Menu> menus = menuMapper.selectActiveByUserId(userId);
        return menus.stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MenuVO detail(String id) {
        if (id == null || id.isEmpty()) {
            throw new BusinessException("E9101", "菜单ID不能为空。");
        }
        Menu menu = menuMapper.selectById(id);
        if (menu == null) {
            throw new BusinessException("E9404", "菜单不存在。");
        }
        return toVO(menu);
    }

    @Override
    public MenuVO create(MenuCreateRequest request, String operatorId) {
        Menu menu = new Menu();
        String menuId = UUID.randomUUID().toString();
        menu.setId(menuId);
        menu.setParentId(request.getParentId());
        menu.setLabel(request.getLabel());
        menu.setIcon(request.getIcon());
        menu.setPageTitle(request.getPageTitle());
        menu.setPageSrc(request.getPageSrc());
        menu.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        menu.setIsDivider(request.getIsDivider() != null ? request.getIsDivider() : false);
        menu.setStatus(request.getStatus() != null ? request.getStatus() : "active");
        menu.setCreatedBy(operatorId);

        menuMapper.insert(menu);

        if (operatorId != null) {
            permissionMapper.insertUserMenu(operatorId, menuId);
        }

        String pageSrc = request.getPageSrc();
        String[][] actions = PAGE_ACTIONS.getOrDefault(pageSrc, DEFAULT_ACTIONS);
        for (String[] act : actions) {
            String actionId = UUID.randomUUID().toString();
            permissionMapper.insertAction(actionId, menuId, act[0], act[1]);
        }

        return toVO(menu);
    }

    @Override
    public MenuVO update(String id, MenuUpdateRequest request, String operatorId) {
        Menu existing = menuMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("E9404", "菜单不存在。");
        }

        LocalDateTime currentUpdatedAt = menuMapper.selectUpdatedAtById(id);
        String currentUpdatedAtStr = currentUpdatedAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        if (!Objects.equals(currentUpdatedAtStr, request.getUpdatedAt())) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        Menu updateMenu = new Menu();
        updateMenu.setId(id);
        updateMenu.setParentId(request.getParentId());
        updateMenu.setLabel(request.getLabel());
        updateMenu.setIcon(request.getIcon());
        updateMenu.setPageTitle(request.getPageTitle());
        updateMenu.setPageSrc(request.getPageSrc());
        updateMenu.setSortOrder(request.getSortOrder());
        updateMenu.setIsDivider(request.getIsDivider());
        updateMenu.setStatus(request.getStatus());
        updateMenu.setUpdatedAt(currentUpdatedAt);
        updateMenu.setUpdatedBy(operatorId);

        int affected = menuMapper.updateByIdAndUpdatedAt(updateMenu);
        if (affected == 0) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        Menu updated = menuMapper.selectById(id);
        return toVO(updated);
    }

    @Override
    public void delete(String id, String updatedAt, String operatorId) {
        Menu existing = menuMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("E9404", "菜单不存在。");
        }

        LocalDateTime currentUpdatedAt = menuMapper.selectUpdatedAtById(id);
        String currentUpdatedAtStr = currentUpdatedAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        if (!Objects.equals(currentUpdatedAtStr, updatedAt)) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        int affected = menuMapper.logicDeleteByIdAndUpdatedAt(id, currentUpdatedAt, operatorId);
        if (affected == 0) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }
    }

    @Override
    public MenuVO updateStatus(String id, String status, String updatedAt, String operatorId) {
        Menu existing = menuMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("E9404", "菜单不存在。");
        }

        LocalDateTime currentUpdatedAt = menuMapper.selectUpdatedAtById(id);
        String currentUpdatedAtStr = currentUpdatedAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        if (!Objects.equals(currentUpdatedAtStr, updatedAt)) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        Menu updateMenu = new Menu();
        updateMenu.setId(id);
        updateMenu.setStatus(status);
        updateMenu.setUpdatedAt(currentUpdatedAt);
        updateMenu.setUpdatedBy(operatorId);

        int affected = menuMapper.updateStatusByIdAndUpdatedAt(updateMenu);
        if (affected == 0) {
            throw new BusinessException("E9409", "数据已被其他用户修改，请刷新后重试。");
        }

        Menu updated = menuMapper.selectById(id);
        return toVO(updated);
    }

    private MenuVO toVO(Menu menu) {
        MenuVO vo = new MenuVO();
        vo.setId(menu.getId());
        vo.setParentId(menu.getParentId());
        vo.setLabel(menu.getLabel());
        vo.setIcon(menu.getIcon());
        vo.setPageTitle(menu.getPageTitle());
        vo.setPageSrc(menu.getPageSrc());
        vo.setSortOrder(menu.getSortOrder());
        vo.setIsDivider(menu.getIsDivider());
        vo.setStatus(menu.getStatus());
        vo.setCreatedAt(menu.getCreatedAt() != null ? menu.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        vo.setUpdatedAt(menu.getUpdatedAt() != null ? menu.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null);
        return vo;
    }
}
