<template>
  <div>
    <el-menu
        :default-active="activeIndex2"
        class="el-menu-demo"
        mode="horizontal"
        @select="handleSelect"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
    >
      <div style="float: left;margin-left: 20px;clear:left; ">
        <img class="mlogo" src="../../assets/logo.jpg" alt="logo图片"/>
      </div>
<!--      <span style="color: #ffffff;font-size: 24px;margin: 5px auto">Arthur的个人博客</span>-->
      <div style="float: right;clear: right; ">
        <el-submenu index="1" style="float: left">
          <template slot="title">
            <div>
              <el-avatar :src="user.avatar" class="headerRightAvatar"></el-avatar>
              <span class="headerRight">欢迎您,{{ user.username }}</span>
            </div>
          </template>
          <el-menu-item index="1-1">个人中心</el-menu-item>
          <el-menu-item index="1-2">账户设置</el-menu-item>
          <el-menu-item index="1-3">密码修改</el-menu-item>
        </el-submenu>
        <el-menu-item index="2" style="float: right"><a @click="logout">注销</a></el-menu-item>
      </div>

    </el-menu>
  </div>
</template>

<script>
export default {
  name: "SystemHead",
  data() {
    return {
      user: {
        username: '',
        avatar: ''
      },
      activeIndex2: '1'
    }
  },
  created() {
    if (this.$store.getters.getUser.username) {
      //如果用户名不为空
      this.user.username = this.$store.getters.getUser.username;
      this.user.avatar = this.$store.getters.getUser.avatar;
      // console.log(this.$store.getters.getUser.avatar);
    }
  },
  methods: {
    //退出操作
    logout() {
      const _this = this;
      //首先调用后端logout接口（因该接口需要认证权限，所以需要传入token）
      //其次调用$store清除用户信息及token
      _this.axios.get("/logout", {
        headers: {
          "Authorization": localStorage.getItem("token")
        }
      }).then(res => {
        console.log(res);
        _this.$store.commit("REMOVE_INFO");
        _this.$router.push("/login");
      })
    },
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    }
  }
}
</script>

<style scoped>

.mlogo {
  margin-top: 7px;
  width: 40px;
  height: 40px;
}

.headerRightAvatar {
  margin-top: -5px;
  margin-right: 5px;
}

span.headerRight {
  margin-top: -10px;
  color: #ffffff;
}

/deep/ .el-menu.el-menu--horizontal {
  border-bottom: 0;
}
</style>
