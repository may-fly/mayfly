# Mayfly Admin

> 前端项目

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
	|   |-- assets                       // 静态资源，你的style、图片、字体等。
	|   |-- components                   // 全局组件，其他页面组件请归类到所在目录
	|       |-- HelpHint                 // 帮助组件
	|       |-- Pagination               // 分页组件，你只需提供一个api，他就能完成分页的所有事情
	|       |-- ScrollPane               // 滚动组件，鼠标滚动左右移动容器，TabBar上有用
	|       |-- ToolBar                  // 每个页面的工具栏，可以在这里做权限控制，添加权限之类的
	|   |-- menu                         // 后台菜单配置，包括使用的图标等，里面也可以做权限控制
	|   |-- router                       // vue-route,当项目比较大，路由较多也建议分为多个文件，里面有例子。
	|   |-- store                        // vuex,数据仓库,model之类的
	|   |-- theme                        // ElementUI 的定制主题，喜欢折腾的朋友可以自己弄
	|   |-- views                        // 页面视图。里面文件夹建议使用大驼峰,因为这样比较好看 - - 
	|       |-- Layout                   // 主页面布局视图
	|       |-- Home                     // 后台主页
	|       |-- Permission          // 这个目录比较重要，建议不要去修改，这里正在开发，配合go语言会做一个自动生成代码的工具，里面有一些模板等。
	|       |-- xxxx                     // 不一一介绍了，自己看吧
	|   |-- App.vue                      // 页面入口文件
	|   |-- index.html                   // index.html模板
	|   |-- main.js                      // 程序入口文件，加载各种公共组件
	|-- .babelrc                         // ES6语法编译配置
	|-- .gitignore                       // 忽略的文件
	|-- package.json                     // 项目及工具的依赖配置文件
	|-- README.md                        // 说明


