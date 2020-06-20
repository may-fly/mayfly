const resolve = require('path').resolve
const webpack = require('webpack')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const url = require('url')
const publicPath = ''
const VueLoaderPlugin = require('vue-loader/lib/plugin')

module.exports = (options = {}) => ({
  entry: {
    app: './src/main.ts'
  },
  stats: { children: false },
  output: {
    path: resolve(__dirname, 'dist'),
    filename: options.dev ? '[name].js' : '[name].js?[chunkhash]',
    chunkFilename: '[id].js?[chunkhash]',
    publicPath: options.dev ? '/assets/' : publicPath
  },
  module: {
    rules: [{
      test: /\.vue$/,
      use: ['vue-loader']
    },
    {
      test: /\.js$/,
      use: ['babel-loader'],
      exclude: /node_modules/,
    },
    {
      test: /\.tsx?$/,
      loader: 'ts-loader',
      exclude: /node_modules/,
      options: {
        appendTsSuffixTo: [/\.vue$/],
      }
    },
    {
      test: /\.css$/,
      use: ['style-loader', 'css-loader', 'postcss-loader']
    },
    {
      test: /\.(png|jpg|jpeg|gif|eot|ttf|woff|woff2|svg|svgz)(\?.+)?$/,
      use: [{
        loader: 'url-loader',
        options: {
          esModule: false,
          limit: 10000,
          outputPath: 'images/'
        }
      }]
    },
    {
      test: /\.less$/,
      use: [{
        loader: "style-loader" // creates style nodes from JS strings
      }, {
        loader: "css-loader" // translates CSS into CommonJS
      }, {
        loader: "less-loader" // compiles Less to CSS
      }]
    },
      // {
      //   test: /\.html$/,
      //   use: [{
      //     loader: 'html-loader',
      //     options: { // 配置html中图片编译
      //       minimize: true
      //     }
      //   }]
      // }
    ]
  },
  optimization: {
    runtimeChunk: {
      name: 'manifest'
    },
    splitChunks: {
      chunks: 'async',
      cacheGroups: {
        defaultVendors: {
          test: /[\\/]node_modules[\\/]/,
          priority: -10
        },
        default: {
          minChunks: 2,
          priority: -20,
          reuseExistingChunk: true
        }
      }
    }
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: 'src/index.html',
      favicon: 'src/assets/images/favicon.ico'
    }),
    // new webpack.DefinePlugin({
    //   __DEV__: options.dev ? true : false
    // }),
    new VueLoaderPlugin(),
    new webpack.ProgressPlugin()
  ],
  resolve: {
    // 导入时可忽略.ts等结尾
    extensions: ['.ts', '.tsx', '.json', '.js'],
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  devServer: {
    disableHostCheck: true,
    host: 'localhost',
    port: 8016,
    open: true,
    proxy: {
      '/mayfly': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        pathRewrite: {
          '^/mayfly/': '/mayfly/'
        }
      }
    },
    historyApiFallback: {
      index: url.parse(options.dev ? '/assets/' : publicPath).pathname
    }
  },
  devtool: options.dev ? '#eval-source-map' : '#source'
})
