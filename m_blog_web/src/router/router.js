import { createRouter,createWebHistory } from "vue-router";
// import HelloWorld from '../components/HelloWorld.vue';
import Demo from "@/views/test/Demo";
import BlogsList from "@/views/BlogsList";
import Login from "@/views/Login";
import BlogEdit from "@/views/BlogEdit";
import BlogDetail from "@/views/BlogDetail";
import BlogHeader  from "@/views/Header";

const router = createRouter({
    history:createWebHistory(),
    routes:[
        /*{
            path:'/',
            component:HelloWorld,
        },*/
        {
            path:'/Demo',
            name:'DemoTest',
            component:Demo
        },
        {
            path:'/',
            name:'Index',
            redirect:{name: "Blogs"}
        },
        {
            path:'/blogs',
            name:'Blogs',
            component:BlogsList
        },
        {
            path:'/Login',
            name:'Login',
            component:Login
        },
        {
            path:'/blog/add',
            name:'BlogEdit2',
            component:BlogEdit,
            meta:{
                requireAuth: true
            }
        },
        {
            path:'/blog/:blogid',
            name:'BlogDetail',
            component:BlogDetail
        },
        {
            path:'/blog/:blogid/edit',
            name:'BlogEdit',
            component:BlogEdit,
            meta:{
                requireAuth: true
            }
        },
        {
            path:'/BlogHeader',
            name:'BlogHeader',
            component:BlogHeader
        },
    ]
})
export default router;
