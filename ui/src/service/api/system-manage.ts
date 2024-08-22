import { request } from '../request';

/** get role list */
export function fetchGetRoleList(params?: Api.SystemManage.RoleSearchParams) {
  return request<Api.SystemManage.RoleList>({
    url: '/admin-api/system/role/page',
    method: 'post',
    data: params
  });
}

/** add role */
export function addRole(data: any) {
  return request<any>({
    url: '/sys/role/add',
    method: 'post',
    data
  });
}

/** edit role */
export function editRole(data: any) {
  return request<any>({
    url: '/sys/role/edit',
    method: 'put',
    data
  });
}

/** delete role */
export function deleteRole(id: number) {
  return request<any>({
    url: '/sys/role/delete',
    method: 'delete',
    params: {
      id
    }
  });
}

/**
 * get all roles
 *
 * these roles are all enabled
 */
export function fetchGetAllRoles() {
  return request<Api.SystemManage.AllRole[]>({
    url: '/admin-api/system/role/list-all-simple',
    method: 'get'
  });
}

/** get user list */
export function fetchGetUserList(data?: Api.SystemManage.UserSearchParams) {
  return request<Api.SystemManage.UserList>({
    url: '/admin-api/system/user/page',
    method: 'post',
    data
  });
}

/** add user */
export function addUser(data: any) {
  return request<any>({
    url: '/admin-api/system/user/add',
    method: 'post',
    data
  });
}

/** edit user */
export function editUser(data: any) {
  return request<any>({
    url: '/admin-api/system/user/update',
    method: 'put',
    data
  });
}

/** delete user */
export function deleteUser(id: number) {
  return request<any>({
    url: '/admin-api/system/user/delete',
    method: 'delete',
    params: {
      id
    }
  });
}

/** assign roles to user */
export function assignRoleToUser(roleIds: number[], id: number) {
  return request<any>({
    url: '/admin-api/system/permission/assign-user-role',
    method: 'put',
    data: {
      roleIds,
      userId: id
    }
  });
}

/** set user password */
export function resetUserPwd(password: string, id: number) {
  return request<any>({
    url: '/admin-api/system/user/reset-password',
    method: 'put',
    data: {
      password,
      id
    }
  });
}

/** get menu list */
export function fetchGetMenuList() {
  return request<Api.SystemManage.MenuList>({
    url: '/admin-api/system/menu/list',
    method: 'get'
  });
}

/** get all pages */
export function fetchGetAllPages() {
  return request<string[]>({
    url: '/systemManage/getAllPages',
    method: 'get'
  });
}

/** get menu tree */
export function fetchGetMenuTree() {
  return request<Api.SystemManage.MenuTree[]>({
    url: '/sys/menu/all-simple-menu-tree',
    method: 'get'
  });
}

/** get menu by role */
export function fetchGetMenuByRole(id: number) {
  return request<number[]>({
    url: '/sys/menu/list-by-role',
    method: 'get',
    params: {
      id
    }
  });
}

/** assign menu to role */
export function assignMenuToRole(menuIds: number[], roleId: number) {
  return request<any>({
    url: '/sys/role/assign-menus',
    method: 'put',
    data: {
      menuIds,
      roleId
    }
  });
}

/** add menu data */
export function addMenu(menu: any) {
  return request<any>({
    url: '/sys/menu/add',
    method: 'post',
    data: menu
  });
}

/** edit menu data */
export function editMenu(menu: any) {
  return request<any>({
    url: '/sys/menu/edit',
    method: 'put',
    data: menu
  });
}

/** delete menu data */
export function deleteMenu(id: number) {
  return request<any>({
    url: '/sys/menu/delete',
    method: 'delete',
    params: {
      id
    }
  });
}
