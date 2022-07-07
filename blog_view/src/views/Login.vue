<template>
  <div class="bg">
    <!--        <el-container>-->
    <!--          <el-header>-->
    <!--            <img class="mlogo" src="../assets/logo.jpg" alt="logo图片"/>-->
    <!--          </el-header>-->
    <!--          <el-main>-->
    <!--          </el-main>-->
    <!--        </el-container>-->
    <div class="box">
      <div style="color: #111111">
        <h1>登陆</h1>
      </div>
      <div class="bg1">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="78px" class="demo-ruleForm">
          <el-form-item label="用户姓名" prop="username" class="user">
            <el-input v-model="ruleForm.username" @keyup.enter.native="submitForm('ruleForm')"></el-input>
          </el-form-item>
          <el-form-item label="用户密码" prop="password" class="user">
            <el-input type="password" v-model="ruleForm.password" @keyup.enter.native="submitForm('ruleForm')"></el-input>
          </el-form-item>
          <el-form-item>
            <!--            <button id="btn">测试</button>-->
            <el-button type="primary" @click="submitForm('ruleForm')" class="btn">登陆
            </el-button>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
            <!--            <el-button @click="increment">注销</el-button>-->
          </el-form-item>
        </el-form>
      </div>

      <!--      </el-main>-->
      <!--    </el-container>-->
    </div>

  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      ruleForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [
          {required: true, message: '请输入用户名称', trigger: 'blur'},
          {min: 3, max: 15, message: '长度在3到15个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    test(){
      alert("test");
    },
    submitForm(formName) {
      console.log(formName);
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // alert("submit!");
          //获取整个vue的this
          const _this = this;
          // console.log("校验成功");
          // console.log(_this.ruleForm);
          _this.axios.post("/login", _this.ruleForm).then((res) => {
            // console.log("================");
            // console.log(res.data.data);

            const jwt = res.headers['authorization'];
            const userInfo = res.data.data;
            //存储（共享）全局变量jwt和userInfo
            _this.$store.commit("SET_TOKEN", jwt);
            _this.$store.commit("SET_USERINFO", userInfo);

            _this.$router.push('/systemHomePage')
            // console.log(_this.$store.getters.getToken);
            // console.log(_this.$store.getters.getUser);
          }).catch((error) => {
            console.log(error);
          });
        } else {
          console.log('error submit!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    increment() {
      this.axios.get("/logout").then((res) => {
        console.log(res.data.data);
      }).catch((error) => {
        console.log(error);
      })
    },
    keyupEnter(formName) {
      document.onkeydown = e => {
        let body = document.getElementsByTagName('body')[0]
        if (e.keyCode === 13 && e.target.baseURI.match(/login/) && e.target === body) {
          console.log('enter') // match(此处应填写文件在浏览器中的地址，如 '/home/index')
          this.submitForm(formName);
          // console.log(formName);
        }
      }
    },
  },
  created() {
    this.keyupEnter('ruleForm')//页面在创建时就调用键盘的回车事件，在结构代码中也可以不写@keyup.enter.native="handleAddBook"
  }
}
</script>

<style scoped>
.el-header,
.el-footer {
  background-color: #b3c0d1;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-main {
  /* background-color: #e9eef3; */
  color: #333;
  text-align: center;
  line-height: 160px;
}

.mlogo {
  height: 80%;
}

.demo-ruleForm {
  max-width: 500px;
  margin: 0 auto;
}

.bg {
  background-image: url("http://116.62.14.32:8080/group1/M00/00/00/rBow6mKPKKWABmkwAAQDl562aMA555.jpg");
  width: 100%;
  min-height: 100vh;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;
}

/deep/ .user .el-form-item__label {
  color: #111;
}

.box {
  position: absolute;

  top: 50%;

  transform: translate(0, -50%);
}

.bg1 {
  padding-top: 40px;
  width: 550px;
  height: 200px;
  background-color: #ffffff;
  border-radius: 10px;
  text-align: center;
}
</style>
