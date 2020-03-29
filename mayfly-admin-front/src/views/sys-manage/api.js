import Api from '~/common/Api';

export const resourceApi = {
    list: Api.url("/sys/resources").method('get'),
    detail: Api.url("/sys/resources/{id}").method('get'),
    save: Api.url("/sys/resources").method('post'),
    update: Api.url("/sys/resources/{id}").method('put'),
    del: Api.url("/sys/resources/{id}").method('delete'),
    changeStatus: Api.url("/sys/resources/{id}/{status}").method('put')
}

export const roleApi = {
    list: Api.url("/sys/roles").method('get'),
    save: Api.url("/sys/roles").method('post'),
    update: Api.url("/sys/roles/{id}").method('put'),
    del: Api.url("/sys/roles/{id}").method('delete'),
    // 获取指定角色拥有的资源id
    roleResourceIds: Api.url("/sys/roles/{id}/resourceIds").method('get'),
    roleResources: Api.url("/sys/roles/{id}/resources").method('get'),
    saveResources: Api.url("/sys/roles/{id}/resources").method('post')
}

export const accountApi = {
    list: Api.url("/sys/accounts").method('get'),
    save: Api.url("/sys/accounts").method('post'),
    del: Api.url("/sys/accounts/{id}").method('delete'),
    changeStatus: Api.url("/sys/accounts/{id}/{status}").method('put'),
    roles: Api.url("/sys/accounts/{id}/roles").method('get'),
    saveRoles: Api.url("/sys/accounts/{id}/roles").method('post')
}

export const logApi = {
    list: Api.url("/sys/logs").method("get")
}
