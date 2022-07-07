import Vue from 'vue'
import Router from 'vue-router'
import Demo from '../views/test/Demo'
import BlogsList from '../views/BlogsList'
import Login from '../views/Login'
import BlogEdit from '../views/BlogEdit'
import BlogDetail from '../views/BlogDetail'
import Header from '../views/Header'
import SystemHomePage from "@/views/backgroundSystem/SystemHomePage";
import BlogList from "@/views/admin/BlogList";
import BlogsEdit from "@/views/admin/BlogsEdit";
import CategoryList from "@/views/admin/CategoryList";
import CommentList from "@/views/admin/CommentList";
import FriendList from "@/views/admin/FriendList";
import UserManager from "@/views/admin/UserManager";
import VisitLog from "@/views/admin/VisitLog";
import VisitorList from "@/views/admin/VisitorList";
import TagList from "@/views/admin/TagList";
import Home from "@/views/Home";
import Blog from "@/views/Blog";
import Archives from "@/views/Archives";
import About from "@/views/About";
import Friends from "@/views/Friends";
import Category from "@/views/Category";
import Tags from "@/views/Tags";
import Params from "@/views/test/Params";

Vue.use(Router)

  const routes= [
    {
      path: '/Demo',
      name: 'DemoTest',
      component: Demo
    },
    {
      path: '/params',
      name: 'params',
      component: Params
    },
    {
      path: '/Login',
      name: 'Login',
      component: Login,
      meta: {
        title: '登陆'
      }
    },
    /*{
      path: '/',
      name: 'Index',
      redirect: {name: 'Blogs'}
    },
    {
      path: '/blogs',
      name: 'Blogs',
      component: BlogsList,
      meta: {
        title: '主页'
      }
    },

    {
      path: '/blog/add',
      name: 'BlogEdit2',
      component: BlogEdit,
      meta: {
        title: '添加',
        requireAuth: true
      }
    },
    {
      path: '/blog/:blogid',
      name: 'BlogDetail',
      component: BlogDetail,
      meta: {
        title: '详情'
      }
    },
    {
      path: '/blog/:blogid/edit',
      name: 'BlogEdit',
      component: BlogEdit,
      meta: {
        title: '编辑',
        requireAuth: true
      }
    },*/
    {
      path: '/systemHomePage',
      name: 'SystemHomePage',
      component: SystemHomePage,
      meta: {
        title: '博客后台系统'
      }
    },
    /*{
      path: '/BlogHeader',
      name: 'BlogHeader',
      component: Header
    },*/
    {
      path: '/systemHomePage',
      name: 'SystemHomePage',
      component: SystemHomePage,
      meta: {
        title: '后台管理',
        requireAuth: true
      },
      children:[
        {
          path: '/writeBlog',
          name: 'writeBlog',
          component: BlogsEdit,
          meta: {
            title: '写文章',
            requireAuth: true
          }
        },
        {
          path: '/blogList',
          name: 'blogList',
          component: BlogList,
          meta: {
            title: '文章管理',
            requireAuth: true
          }
        },
        {
          path: '/blog/edit/:blogId',
          name: 'BlogEdit3',
          component: BlogsEdit,
          meta: {
            title: '编辑博客',
            requireAuth: true
          }
        },
        {
          path: '/type',
          component: CategoryList,
          name: 'type',
          meta: {
            requireAuth: true,
            title: '分类管理'
          }
        },
        {
          path: '/tag',
          component: TagList,
          name: 'tag',
          meta: {
            requireAuth: true,
            title: '标签管理'
          }
        },
        {
          path: '/comments',
          component: CommentList,
          name: 'comments',
          meta: {
            title: '评论管理',
            requireAuth: true
          }
        },
        {
          path: '/friendList',
          component: FriendList,
          name: 'friendList',
          meta: {
            requireAuth: true,
            title: '友链管理'
          }
        },
        {
          path: '/userManager',
          component: UserManager,
          name: 'userManager',
          meta: {
            requireAuth: true,
            title: '用户管理'
          }
        },
        {
          path: '/visitLog',
          component: VisitLog,
          name: 'visitLog',
          meta: {
            title: '访问日志',
            requireAuth: true
          }
        },
        {
          path: '/visitor',
          component: VisitorList,
          name: 'visitor',
          meta: {
            title: '访客统计',
            requireAuth: true
          }
        }
      ]
    },
    {
      path: '/',
      name: 'Index',
      //懒加载
      component: () => import('../views/Index'),
      redirect: 'Home',
      children: [
        {
          path: '',
          name: 'Home',
          component: Home,
          meta: {
            title: '首页'
          }
        },
        {
          path: '/blog/:blogId',
          name: 'Blog',
          component: Blog,
          meta: {
            title: '博客详情'
          }
        },
        {
          path: '/archives',
          name: 'Archives',
          component: Archives,
          meta: {
            title: '归档'
          }
        },
        {
          path: '/about',
          name: 'About',
          component: About,
          meta: {
            title: '关于我'
          }
        },
        {
          path: 'friends',
          name: 'Friends',
          component: Friends,
          meta: {
            title: '友链'
          }
        },
        {
          path: '/category/:name',
          name: 'Category',
          component: Category,
          meta: {
            title: '分类'
          }
        },
        {
          path: '/tag/:name',
          name: 'Tags',
          component: Tags,
          meta: {
            title: '标签'
          }
        }
      ]
    }
  ]

const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
})

router.afterEach((to, from) => {
  // 在全局后置守卫中获取路由元信息设置title
  document.title = to.meta.title
})

//清除相同路由是报错
const VueRouterPush = Router.prototype.push
Router.prototype.push = function push (to) {
  return VueRouterPush.call(this,to).catch(err => err)
}

export default router
