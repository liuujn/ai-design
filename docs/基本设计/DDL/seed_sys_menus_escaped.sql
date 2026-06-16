-- 初始菜单数据
-- is_divider: 1=刁E��线(刁E��E��E��E, 0=普通菜单项

INSERT INTO sys_menus (id, label, icon, page_title, page_src, sort_order, is_divider, status, created_by)
VALUES (''m001'', ''首页'',
        ''<svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"/></svg>'',
        ''系统首页'', NULL, 1, 0, ''active'', ''SYSTEM'');

INSERT INTO sys_menus (id, label, sort_order, is_divider, status, created_by)
VALUES (''m002'', ''业务管琁E, 2, 1, ''active'', ''SYSTEM'');

INSERT INTO sys_menus (id, label, icon, page_title, page_src, sort_order, is_divider, status, created_by)
VALUES (''m003'', ''用户管琁E,
        ''<svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197m13.5-9a2.5 2.5 0 11-5 0 2.5 2.5 0 015 0z"/></svg>'',
        ''用户管琁E, ''用户.html'', 3, 0, ''active'', ''SYSTEM'');

INSERT INTO sys_menus (id, label, icon, page_title, page_src, sort_order, is_divider, status, created_by)
VALUES (''m004'', ''地址管琁E,
        ''<svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/></svg>'',
        ''地址管琁E, ''地址.html'', 4, 0, ''active'', ''SYSTEM'');

INSERT INTO sys_menus (id, label, icon, page_title, page_src, sort_order, is_divider, status, created_by)
VALUES (''m005'', ''啁E��刁E��'',
        ''<svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z"/></svg>'',
        ''啁E��刁E��'', ''啁E��刁E��.html'', 5, 0, ''active'', ''SYSTEM'');

INSERT INTO sys_menus (id, label, icon, page_title, page_src, sort_order, is_divider, status, created_by)
VALUES (''m006'', ''啁E��管琁E,
        ''<svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"/></svg>'',
        ''啁E��管琁E, ''啁E��.html'', 6, 0, ''active'', ''SYSTEM'');

INSERT INTO sys_menus (id, label, icon, page_title, page_src, sort_order, is_divider, status, created_by)
VALUES (''m007'', ''订单管琁E,
        ''<svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"/></svg>'',
        ''订单管琁E, ''订十Ehtml'', 7, 0, ''active'', ''SYSTEM'');

INSERT INTO sys_menus (id, label, icon, page_title, page_src, sort_order, is_divider, status, created_by)
VALUES (''m008'', ''购物车'',
        ''<svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 100 4 2 2 0 000-4z"/></svg>'',
        ''购物车管琁E, ''购物车.html'', 8, 0, ''active'', ''SYSTEM'');

INSERT INTO sys_menus (id, label, icon, page_title, page_src, sort_order, is_divider, status, created_by)
VALUES (''m009'', ''系统管琁E,
        NULL, NULL, NULL, 9, 1, ''active'', ''SYSTEM'');

INSERT INTO sys_menus (id, label, icon, page_title, page_src, sort_order, is_divider, status, created_by)
VALUES (''m010'', ''菜单管琁E,
        ''<svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"/></svg>'',
        ''菜单管琁E, ''菜单管琁Ehtml'', 10, 0, ''active'', ''SYSTEM'');

