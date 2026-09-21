webpack打包的流程

1. 安装 Webpack：
	npm install webpack webpack-cli --save-dev
2. 创建 Webpack 配置文件 webpack.config.js
	```
	const path = require('path');

	module.exports = {
  	entry: './src/index.js',
  	output: {
    	path: path.resolve(__dirname, 'dist'),
    	filename: 'bundle.js'
  	},
  	// 可以添加其他需要的配置
	};

	```
3. 配置加载器（Loaders）
	根据项目需要，在配置文件中添加各种加载器来处理不同类型的文件，
	比如 Babel 处理 ES6、CSS、图片等

	```
	module: {
	  rules: [
	    {
	      test: /\.js$/,
	      exclude: /node_modules/,
	      use: {
	        loader: 'babel-loader'
	      }
	    },
	    // 其他加载器配置
	  ]
	}
	```
4. 安装必要的加载器和插件
5. 运行打包
	npx webpack --config webpack.config.js
6. 查看打包结果


