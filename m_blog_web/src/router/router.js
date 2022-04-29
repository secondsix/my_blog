import { createRouter,createWebHistory } from "vue-router";
import HelloWorld from '../components/HelloWorld.vue';
import Demo from "@/views/test/Demo";
const router = createRouter({
    history:createWebHistory(),
    routes:[
        {
            path:'/',
            component:HelloWorld,
        },
        {
            path:'/Demo',
            name:'DemoTest',
            component:Demo
        }
    ]
})
export default router;
