/*module.exports = {
    //Solution For Issue:You are using the runtime-only build of Vue where the template compiler is not available. Either pre-compile the templates into render functions, or use the compiler-included build.
    //zhengkai.blog.csdn.net
    runtimeCompiler: true
}*/
const webpack = require("webpack");
module.exports = {
    publicPath: '/',
    configureWebpack: {
        plugins: [
            new webpack.ProvidePlugin({
                $: "jquery",
                jQuery: "jquery",
                "window.jQuery": "jquery",
                "window.$": "jquery",
                Popper: ["popper.js", "default"]
            })
        ]
    },
    runtimeCompiler: true,
    devServer:{
        host: 'localhost',
        port:8081,  // 端口号的配置
        proxy: {
            '/api': {
                // 后台地址请求，加 http / https 请求本地
                target: 'http://116.62.14.32:8083',
                // 是否跨域
                changeOrigin: true,
                ws: false,
                secure: false,
                // 重定向
                pathRewrite: {
                    // 将请求地址中前面的替换为后面的
                    '^/api': '/'
                }
            }
        }

    }
}
