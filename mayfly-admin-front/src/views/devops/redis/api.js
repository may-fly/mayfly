import Api from '~/common/Api';

export const redisApi = {
    // 获取权限列表
    list: Api.url("/devops/redis").method('get'),
    info: Api.url("/devops/redis/{id}/info").method('get'),
    // 删除权限
    del: Api.url("/devops/redis/{id}").method('delete'),
    // 保存按钮
    save: Api.url("/devops/redis").method('post'),
    update: Api.url("/devops/redis/{id}").method('put')
}

export const redisKeyApi = {
    scan: Api.url("/devops/redis/{cluster}/{id}/scan").method('get'),
    value: Api.url("/devops/redis/{cluster}/{id}/value").method('get'),
    update: Api.url("/devops/redis/{cluster}/{id}").method('put'),
    del: Api.url("/devops/redis/{cluster}/{id}").method('delete')
}