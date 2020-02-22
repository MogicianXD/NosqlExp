module.exports = {
  lintOnSave: false, 
  devServer: {
    https: false,
    publicPath: '/', // 设置打包文件相对路径
    proxy: {
      '/NosqlExp': {
        target: 'http://127.0.0.1:8091/', //对应自己的接口
        changeOrigin: true,
        ws: true
      }
    }
  }
}
