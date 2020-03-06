import Permission from '~/common/Permission'

/**
 * 权限code
 */
const machine = {
  // 获取权限列表
  machine: Permission.code("machine"),
  // 删除机器
  del: Permission.code("machine:delete"),
  // 获取配置文件列表
  machineFile: Permission.code("machineFile"),
  rmFile: Permission.code("machineFile:rm"),
  uploadFile: Permission.code("machineFile:upload"),
  // 修改文件内容
  updateFileContent: Permission.code("machineFile:updateFileContent"),
  // 添加文件or目录
  addConf: Permission.code("machineFile:addConf"),
}


export default machine;
