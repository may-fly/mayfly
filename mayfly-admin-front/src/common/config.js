const devUrl = 'http://127.0.0.1:8080/mayfly';
const devSocketUrl = 'ws://127.0.0.1:8080/mayfly';

const proUrl = 'http://94.191.96.31:8080/mayfly';
const proSocketUrl = 'ws://94.191.96.31:8080/mayfly';

export default {
  apiUrl: __DEV__ ? devUrl : proUrl,
  sockeUrl: __DEV__ ? devSocketUrl : proSocketUrl,
  
  /**
   * 名字命名
   */
  name: {
    // 站点名
    siteName: 'Mayfly Admin',
    // 存在sessionStorage里面的key名称
    tokenKey: 'token',
    resourcesKey: 'resources',
    adminKey: 'admin',
    permissionsKey: 'permissions',
  }
}