const devBaseUri = 'localhost:8080/mayfly';
const prodBaseUri = "mayfly.1yue.net/mayfly";

const devUrl = `http://${devBaseUri}`;
const devSocketUrl = `ws://${devBaseUri}`;

const proUrl = `http://${prodBaseUri}`;
const proSocketUrl = `ws://${prodBaseUri}`;

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