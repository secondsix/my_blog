import { createApp } from 'vue'
import App from './App.vue'

import router from "@/router/router";
import element from 'element-plus';
import 'element-plus/theme-chalk/index.css';
import store from "@/stroe";

import axios from "axios";
import VueAxios from "vue-axios";
//引入自定义axios.js
import "./axios";

//mavonEditor
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'

//路由拦截
import './permission'

createApp(App).use(VueAxios,axios).use(store).use(mavonEditor).use(router).use(element).mount('#app')
