// 引入axios依赖
import axios from 'axios'
// 引入element-ui依赖
import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
// 引入vuex
import store from './store'
// 引入路由
import router from './router'

var s = window.location.toString()
var s1 = s.substr(7, s.length)
var s2 = s1.indexOf('/')
s = s.substr(0, 8 + s2)
// 取连接前缀相当于 http://localhost:8083/
var a = 'http://localhost:8083/'
// 用于部署在服务器上
// var a = 'http://116.62.14.32:8083/'

// 配置默认前缀
axios.defaults.baseURL = a

// 配置前置拦截
axios.interceptors.request.use(config => {
  return config
})

// 配置后置拦截
axios.interceptors.response.use(response => {
  let res = response.data
  if (res.code === 200) {
    return response
  } else {
    Element.Message.error('操作错了哦', {duration: 2 * 1000})
    // 返回一个异常提示就不会继续往下走了 不+的话 res=>的里面 还是会继续走
    return Promise.reject(response.data.msg)
  }
}, error => {
  // 使得异常信息更加友好
  console.log(error)
  if (error.response.data) {
    // data不为空时
    error.message = error.response.data.msg
    console.log('-------------------------')
    console.log(error.message)
    console.log('-------------------------')
  }

  if (error.response.status === 401) {
    // 清空token userInfo
    store.commit('REMOVE_INFO')
    // 跳转登陆页面
    router.push('/login')
  }

  Element.Message.error(error.message)
  return Promise.reject(error)
})
