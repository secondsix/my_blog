<template>
  <div>
<!--    <el-menu
        default-active="1"
        class="el-menu-vertical-demo"
        @open="handleOpen"
        @close="handleClose"
        background-color="#fff"
        text-color="#111"
        active-text-color="#206dfc"
        :router="true"
    >
      <el-menu-item index="1">
        <i class="el-icon-setting"></i>
        <span slot="title">首页</span>
      </el-menu-item>

      <el-submenu index="2">
        <template slot="title">博客管理</template>
        <el-menu-item index="2-1">写文章</el-menu-item>
        <el-menu-item index="2-2">文章管理</el-menu-item>
        <el-menu-item index="2-3">分类管理</el-menu-item>
        <el-menu-item index="2-4">评论管理</el-menu-item>
      </el-submenu>

      <el-submenu index="3">
        <template slot="title">页面管理</template>
        <el-menu-item-group>
          <el-menu-item index="3-1">友链管理</el-menu-item>
        </el-menu-item-group>
      </el-submenu>

      <el-submenu index="4">
        <template slot="title">系统管理</template>
        <el-menu-item-group>
          <el-menu-item index="4-1">用户管理</el-menu-item>
        </el-menu-item-group>
      </el-submenu>

      <el-submenu index="5">
        <template slot="title">日志管理</template>
        <el-menu-item-group>
          <el-menu-item index="5-1">访问日志</el-menu-item>
        </el-menu-item-group>
      </el-submenu>

      <el-submenu index="6">
        <template slot="title">数据统计</template>
        <el-menu-item-group>
          <el-menu-item index="6-1">访问统计</el-menu-item>
        </el-menu-item-group>
      </el-submenu>

    </el-menu>-->

      <el-menu
          :unique-opened="true"
          :router="true"
          background-color="#3a3a3a"
          text-color="#fff"
          active-text-color="#ffd04b"
      >
        <!--  index唯一标识  -->
        <el-submenu :index="''+item1.order" v-for="(item1,index) in menuData" :key="item1.path">
          <!--   表示可以展开的一组   -->
          <template slot="title" @click="clickTitle">
            <i class="el-icon-location"></i>
            <span>{{item1.name}}</span>
          </template>
          <el-menu-item
              class="menuItem"
              @click="clickMenuItem"
              v-for="(item2,index) in item1.children"
              :key="item2.path"
              :index="item2.path"
          >
            <i class="el-icon-location"></i>
            <span>{{item2.name}}</span>
          </el-menu-item>
        </el-submenu>
      </el-menu>

  </div>


</template>

<script>
export default {
  name: "SystemSidebar",
  data() {
    return {
      menuData: [
        {
          name: '首页',
          path: 'main',
          order: '1'
        },
        {
          name: '博客管理',
          path: '/blog/add',
          order: '2',
          children: [
            {
              path: '/blog/add',
              name: '写文章'
            },
            {
              path: '/Demo',
              name: 'demo'
            }
          ]
        }
      ]
    }
  },
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    clickTitle(){

    },
    clickMenuItem(){

    }
  }
}
</script>

<style scoped>

</style>
