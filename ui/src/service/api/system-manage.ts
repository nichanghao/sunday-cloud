import {request} from '../request';

/** get role list */
export function fetchGetRoleList(params?: Api.SystemManage.RoleSearchParams) {
  return request<Api.SystemManage.RoleList>({
    url: '/sys/role/page',
    method: 'post',
    data: params
  });
}

/** add role */
export function addRole(data: any) {
  return request<any>({
    url: '/sys/role/add',
    method: 'post',
    data: data
  });
}

/** edit role */
export function editRole(data: any) {
  return request<any>({
    url: '/sys/role/edit',
    method: 'put',
    data: data
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
    url: '/sys/role/all-simple-roles',
    method: 'get'
  });
}

/** get user list */
export function fetchGetUserList(data?: Api.SystemManage.UserSearchParams) {
  return request<Api.SystemManage.UserList>({
    url: '/sys/user/page',
    method: 'post',
    data: data
  });
}

/** add user */
export function addUser(data: any) {
  return request<any>({
    url: '/sys/user/add',
    method: 'post',
    data: data
  });
}

/** edit user */
export function editUser(data: any) {
  return request<any>({
    url: '/sys/user/edit',
    method: 'put',
    data: data
  });
}

/** delete user */
export function deleteUser(id: number) {
  return request<any>({
    url: '/sys/user/delete',
    method: 'delete',
    params: {
      id
    }
  });
}

/** assign roles to user */
export function assignRoleToUser(roleIds: number[], id: number) {
  return request<any>({
    url: '/sys/user/assign-roles',
    method: 'put',
    data: {
      roleIds,
      id
    }
  });
}

/** set user password */
export function resetUserPwd(password: string, id: number) {
  return request<any>({
    url: '/sys/user/reset-password',
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
