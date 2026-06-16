-- seed menu data (icons are inline SVG via update after insert to keep SQL readable)
insert into sys_menus (id, label, icon, page_title, page_src, sort_order, is_divider, status, created_by)
values ('m001', 'Home', '<svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"/></svg>', 'System Home', null, 1, 0, 'active', 'SYSTEM');

insert into sys_menus (id, label, sort_order, is_divider, status, created_by)
values ('m002', 'Business', 2, 1, 'active', 'SYSTEM');

insert into sys_menus (id, label, icon, page_title, page_src, sort_order, is_divider, status, created_by)
values ('m003', 'User Mgmt', '<svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197m13.5-9a2.5 2.5 0 11-5 0 2.5 2.5 0 015 0z"/></svg>', 'User Management', 'user.html', 3, 0, 'active', 'SYSTEM');

insert into sys_menus (id, label, icon, page_title, page_src, sort_order, is_divider, status, created_by)
values ('m004', 'Address Mgmt', '<svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/></svg>', 'Address Management', 'address.html', 4, 0, 'active', 'SYSTEM');

insert into sys_menus (id, label, icon, page_title, page_src, sort_order, is_divider, status, created_by)
values ('m005', 'Category Mgmt', '<svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 7v10a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2h-6l-2-2H5a2 2 0 00-2 2z"/></svg>', 'Category Management', 'category.html', 5, 0, 'active', 'SYSTEM');

insert into sys_menus (id, label, icon, page_title, page_src, sort_order, is_divider, status, created_by)
values ('m006', 'Product Mgmt', '<svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"/></svg>', 'Product Management', 'product.html', 6, 0, 'active', 'SYSTEM');

insert into sys_menus (id, label, icon, page_title, page_src, sort_order, is_divider, status, created_by)
values ('m007', 'Order Mgmt', '<svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01"/></svg>', 'Order Management', 'order.html', 7, 0, 'active', 'SYSTEM');

insert into sys_menus (id, label, icon, page_title, page_src, sort_order, is_divider, status, created_by)
values ('m008', 'Cart Mgmt', '<svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 100 4 2 2 0 000-4z"/></svg>', 'Cart Management', 'cart.html', 8, 0, 'active', 'SYSTEM');

insert into sys_menus (id, label, sort_order, is_divider, status, created_by)
values ('m009', 'System', 9, 1, 'active', 'SYSTEM');

insert into sys_menus (id, label, icon, page_title, page_src, sort_order, is_divider, status, created_by)
values ('m010', 'Menu Mgmt', '<svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.066 2.573c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.573 1.066c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.066-2.573c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"/><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/></svg>', 'Menu Management', 'menu.html', 10, 0, 'active', 'SYSTEM');

insert into sys_menus (id, label, icon, page_title, page_src, sort_order, is_divider, status, created_by)
values ('m011', 'Perm Mgmt', '<svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"/></svg>', 'Permission Management', 'permission.html', 11, 0, 'active', 'SYSTEM');
commit;
