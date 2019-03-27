#新版本已经使用了最新的vue-cli3重构
https://github.com/xusenlin/ElementUIAdmin2


# Element UI Admin

> 一个干净优雅的Element UI Admin模板

*一个大型单页应用离不开合理的项目结构和一些简单的封装*


## Start

 - 克隆或者下载这个仓库
 - 进入项目目录安装依赖

``` bash
yarn
```

## Develop

``` bash
# serve with hot reload at localhost:8010
npm run dev
```

## Build

``` bash
# build for production with minification
npm run build
```

## 目录结构介绍 ##

	|-- dist                             // 打包目录
	|-- src                              // 源码目录
	|   |-- api                          // Api接口,统一封装支持回调和Promise
	|       |-- user.js           	      //用户登录、头像上传、密码修改等用户相关api
	|       |-- init.js                  // api初始化，基于axios的封装，
	|       |-- index.js           	     // api模块的主文件，在main.js挂载全部api到vue原型上，Vue.prototype.$Api = Api
	|       |-- file.js                 // 文件上传
	|       |-- xxxx.js           	     // 添加自己的api
	|   |-- assets                       // 静态资源，你的style、图片、字体等。
	|   |-- components                   // 全局组件，其他页面组件请归类到所在目录
	|       |-- HelpHint                 // 帮助组件
	|       |-- Pagination               // 分页组件，你只需提供一个api，他就能完成分页的所有事情
	|       |-- ScrollPane               // 滚动组件，鼠标滚动左右移动容器，TabBar上有用
	|       |-- ToolBar                  // 每个页面的工具栏，可以在这里做权限控制，添加权限之类的
	|   |-- config                       // 应用的配置文件
	|       |-- url.js                   // Dev的url和Pro的url
	|       |-- app.js                   // 应用的配置，比如版本、性别对应的key、应用的名字、文章的状态等，可以具体到某个页面的配置，统一管理。
	|       |-- index.js                 // 配置模块的主文件，在main.js挂载全部配置到vue原型上，Vue.prototype.$Config = Config
	|       |-- xxx.js                   // 其他配置文件，建议分类清楚，如post.js用于文章相关配置
	|   |-- function                     // 公共函数。和config,api一个道理，建议分类清楚。
	|   |-- menu                         // 后台菜单配置，包括使用的图标等，里面也可以做权限控制
	|   |-- router                       // vue-route,当项目比较大，路由较多也建议分为多个文件，里面有例子。
	|   |-- store                        // vuex,数据仓库,model之类的
	|   |-- theme                        // ElementUI 的定制主题，喜欢折腾的朋友可以自己弄
	|   |-- views                        // 页面视图。里面文件夹建议使用大驼峰,因为这样比较好看 - - 
	|       |-- Layout                   // 主页面布局视图
	|       |-- Home                     // 后台主页
	|       |-- DevelopmentTool          // 这个目录比较重要，建议不要去修改，这里正在开发，配合go语言会做一个自动生成代码的工具，里面有一些模板等。
	|       |-- xxxx                     // 不一一介绍了，自己看吧
	|   |-- App.vue                      // 页面入口文件
	|   |-- index.html                   // index.html模板
	|   |-- main.js                      // 程序入口文件，加载各种公共组件
	|-- .babelrc                         // ES6语法编译配置
	|-- .gitignore                       // 忽略的文件
	|-- package.json                     // 项目及工具的依赖配置文件
	|-- README.md                        // 说明




## 后话

一直找不到心中最满意的后台模板 UI,为什么不自己做一个最完美的后台模板?从细节开始,Element UI Admin 的目标是干净优雅，快速开发。

*最近做了很多单页应用，当项目越来越大的时候，你会发现刚开始多做一些基础工作，多想想项目合理的结构和一些必要的封装会带来事半功倍的效果。*

(其实很多前端页面都是对某个表的增删查改，代码大同小异，正在考虑为什么不做一个自动化工具，读取表结构，自动生成页面的增删查改?)
如果觉得好的话记得给个star哦 ~~

这个是基于Element UI的后台单页应用模板。点击连接预览http://xusenlin.com/works/eui/

