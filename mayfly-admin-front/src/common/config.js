const devUrl = 'http://127.0.0.1:8080/mayfly';
const proUrl = 'http://apiUrl.com';

export default {
  apiUrl: __DEV__ ? devUrl : proUrl,
  
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