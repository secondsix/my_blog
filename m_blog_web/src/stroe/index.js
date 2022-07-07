import { createApp } from "vue";
import { createStore } from 'vuex';

const store = createStore({
    //定义全局参数，其他页面可以直接获取state里面的内容
    state() {
        return {
            //方法一 localStorage.getItem("token")
            token: '',
            //反序列化获取session会话中的userInfo对象
            userInfo: JSON.parse(sessionStorage.getItem("userInfo"))
        }
    },
    mutations: {
        //相当于实体类的set
        SET_TOKEN:(state,token)=>{
            //将传入的token赋值 给state的token
            state.token = token ;
            //同时可以存入浏览器的localStorage里面
            localStorage.setItem("token",token);
        },
        SET_USERINFO:(state,userInfo)=>{
            //将传入的userInfo赋值给state的userInfo
            state.userInfo = userInfo;
            //同时可以存入会话的sessionStorage里面 sessionStorage中只能存字符串 不能存入对象所以我们存入序列化 json串
            sessionStorage.setItem("userInfo",JSON.stringify(userInfo));
        },
        //删除token及userInfo
        REMOVE_INFO:(state)=>{
            state.token = '';
            state.userInfo = {};
            localStorage.setItem("token","");
            sessionStorage.setItem("userInfo",JSON.stringify(''));
        }
    },
    getters:{
      //相当于get
      //配置一个getUser可以直接获取已经反序列化对象的一个userInfo
      getUser: state => {
          return state.userInfo;
      },
      getToken: state => {
          return state.token;
      }
    },
    actions: {

    },
    modules: {

    }
})

const app = createApp({});
//将store实例作为插件安装
app.use(store);

export default store
