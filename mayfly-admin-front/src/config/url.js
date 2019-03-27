
const devUrl = 'http://127.0.0.1:8080';
const proUrl = 'http://apiUrl.com';


export default {
  apiUrl : __DEV__ ? devUrl : proUrl,
  gitHub : 'https://github.com/xusenlin/ElementUIAdmin'
}

