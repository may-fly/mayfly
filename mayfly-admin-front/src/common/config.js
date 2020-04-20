const devBaseUri = 'localhost:8080';
const prodBaseUri = "mayfly.1yue.net";

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
    menusKey: 'menus',
    codesKey: 'codes',
    adminKey: 'admin',
  }
}