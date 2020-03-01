import Permission from '~/common/Permission'

/**
 * 权限code
 */
const machine = {
  // 获取权限列表
  list: Permission.url("/devops/machines").method('get').code("machines:list"),
  // 保存按钮
  save: Permission.url("/devops/machines").method('post').code("machine:save"),
  update: Permission.url("/devops/machines/{id}").method('put').code("machine:update"),
  // 删除机器
  del: Permission.url("/devops/machines/{id}").method('delete').code("machine:delete"),
  // 服务管理按钮
  serviceManage: new Permission().code("machine:serviceManage"),
  // 获取配置文件列表
  files: Permission.url("/devops/machines/{id}/files").method('get').code("machineFile:files"),
  lsFile: Permission.url("/devops/machines/files/{fileId}/ls").method('get').code("machineFile:ls"),
  rmFile: Permission.url("/devops/machines/files/{fileId}/rm").method('delete').code("machineFile:rm"),
  uploadFile: Permission.url("/devops/machines/files/upload").code("machineFile:upload"),
  fileContent: Permission.url("/devops/machines/files/{fileId}/cat").method('get').code("machineFile:cat"),
  // 修改文件内容
  updateFileContent: Permission.url("/devops/machines/files/{id}").method('put').code("machineFile:updateFileContent"),
  // 添加文件or目录
  addConf: Permission.url("/devops/machines/{machineId}/files").method('post').code("machineFile:addConf"),
  // 删除配置的文件or目录
  delConf: Permission.url("/devops/machines/files/{id}").method('delete').code("machineFile:delConf"),
}


export default machine;
