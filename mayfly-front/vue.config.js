const merge = require("webpack-merge");
const tsImportPluginFactory = require("ts-import-plugin");
const path = require('path')
const htmlWebpackPlugin = require("html-webpack-plugin");

function resolve(dir) {
  return path.join(__dirname, dir)
}

// If your port is set to 80,
// use administrator privileges to execute the command line.
// For example, Mac: sudo npm run
// You can change the port by the following method:
// port = 8000 npm run dev OR npm run dev --port = 8000
const port = process.env.port || process.env.npm_config_port || 8000 // dev port

module.exports = {
  publicPath: '/',
  outputDir: 'dist',
  assetsDir: 'static',
  lintOnSave: false,
  productionSourceMap: false,
  devServer: {
    port: port,
    open: true,
    overlay: {
      warnings: false,
      errors: true
    },
  },
  // // 以下是pwa配置
  // pwa: {
  //   iconPaths: {
  //     favicon32: 'favicon.ico',
  //     favicon16: 'favicon.ico',
  //     appleTouchIcon: 'favicon.ico',
  //     maskIcon: 'favicon.ico',
  //     msTileImage: 'favicon.ico'
  //   }
  // },
  configureWebpack: {
    // provide the app's title in webpack's name field, so that
    // it can be accessed in index.html to inject the correct title.
    name: 'mayfly',
    resolve: {
      alias: {
        '@': resolve('src')
      }
    },
    plugins: [
      new htmlWebpackPlugin({
        template: 'public/index.html',
        favicon: 'public/favicon.ico'
      }),
  
    ]
  },
  transpileDependencies: [
    'vue-echarts',
    'resize-detector'
  ],
  chainWebpack: config => {

    // 自动注入通用的scss，不需要自己在每个文件里手动注入
    // const types = ['vue-modules', 'vue', 'normal-modules', 'normal']
    // types.forEach(type => {
    //   config.module.rule('scss').oneOf(type)
    //   .use('sass-resource')
    //   .loader('sass-resources-loader')
    //   .options({
    //     resources: [
    //       path.resolve(__dirname, './src/assets/styles/global.scss'),
    //     ],
    //   });
    // });
  }
};