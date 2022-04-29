import { createApp } from 'vue'
import App from './App.vue'

import router from "@/router/router";
import element from 'element-plus';
import 'element-plus/theme-chalk/index.css';
import axios from "axios";

createApp(App).use(axios).use(router).use(element).mount('#app')
