<template>
  <el-container>
    <el-header class="header">
      <SystemHead></SystemHead>
    </el-header>
    <el-container>
      <el-aside class="sidebar">
        <el-menu
            :unique-opened="true"
            :router="true"
            background-color="#fff"
            text-color="#111"
            active-text-color="#206dfc"
            :default-openeds="defaultOpeneds"
        >
          <!--  index唯一标识  -->
          <el-submenu :index="''+item1.order" v-for="(item1,index) in menuData" :key="item1.path">
            <!--   表示可以展开的一组   -->
            <template slot="title" @click="clickTitle">
<!--              <i class="el-icon-location"></i>-->
              <span>{{ item1.name }}</span>
            </template>
            <el-menu-item
                class="menuItem"
                @click="clickMenuItem"
                v-for="(item2,index) in item1.children"
                :key="item2.path"
                :index="item2.path"
            >
<!--              <i class="el-icon-location"></i>-->
              <span>{{ item2.name }}</span>
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-container>
        <el-main class="main">
          <router-view></router-view>
        </el-main>
        <el-footer>
          <SystemBottom></SystemBottom>
        </el-footer>
      </el-container>
    </el-container>
  </el-container>
</template>

<script>
import SystemHead from "@/views/backgroundSystem/SystemHead";
import SystemSidebar from "@/views/backgroundSystem/SystemSidebar";
import SystemBottom from "@/views/backgroundSystem/SystemBottom";
import SystemMainBody from "@/views/backgroundSystem/SystemMainBody";

export default {
  name: "SystemHomePage",
  inject: ['reload'],
  components: {
    SystemHead,
    SystemSidebar,
    SystemBottom,
    SystemMainBody
  },
  data() {
    return {
      menuData: [
        {
          name: '博客管理',
          order: '1',
          children: [
            {
              path: '/writeBlog',
              name: '写文章'
            },
            {
              path: '/blogList',
              name: '文章管理'
            },
            {
              path: '/type',
              name: '分类管理'
            },
            {
              path: '/tag',
              name: '标签管理'
            },
            {
              path: '/comments',
              name: '评论管理'
            }
          ]
        },
        {
          name: '页面管理',
          order: '2',
          children: [
            {
              path: '/friendList',
              name: '友链管理'
            }
          ]
        },
        {
          name: '系统管理',
          order: '3',
          children: [
            {
              path: '/userManager',
              name: '用户管理'
            }
          ]
        },
        {
          name: '日志管理',
          order: '4',
          children: [
            {
              path: '/visitLog',
              name: '访问日志'
            }
          ]
        },
        {
          name: '数据统计',
          order: '5',
          children: [
            {
              path: '/visitor',
              name: '访客统计'
            }
          ]
        }
      ],
      style: {
        display: 'block',
      },
      defaultOpeneds: ['1']
    }
  },
  methods: {
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    clickTitle() {

    },
    clickMenuItem() {
      this.reload();
    }
  }
}
</script>

<style scoped>
.header {
  padding: 0;
}

.sidebar {
  flex: 0 0 208px;
  max-width: 208px;
  min-width: 208px;
  width: 208px;
  height: 100vh;
  padding: 0;
  margin-top: -4px;
  margin-bottom: 0;
  border-right: solid 1px #e6e6e6;
}

.main {
  margin-top: -4px;
  padding: 0;
}

/deep/ .el-submenu__title{
  text-align: left;
}

</style>
