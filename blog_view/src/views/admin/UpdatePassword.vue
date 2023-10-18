<template>
  <div>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="78px" class="demo-ruleForm">
      <el-form-item label="旧密码" prop="oldPassword" class="user">
        <el-input type="password" v-model="ruleForm.oldPassword" ></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword" class="user">
        <el-input type="password" v-model="ruleForm.newPassword"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="checkPassword" class="user">
        <el-input type="password" v-model="ruleForm.checkPassword"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')" class="btn">确定</el-button>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "UpdatePassword",

  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.ruleForm.newPassword !== '') {
          this.$refs.ruleForm.validateField('checkPass');
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.ruleForm.newPassword) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      ruleForm: {
        oldPassword: '',
        newPassword: '',
        checkPassword: ''
      },
      rules: {
        oldPassword: [
          {required: true, message: '请输入旧密码', trigger: 'blur'},
          {min: 3, max: 15, message: '长度在3到15个字符', trigger: 'change'},
          {min: 3, max: 15, message: '长度在3到15个字符', trigger: 'blur'},
        ],
        newPassword: [
          {required: true, message: '请输入新密码', trigger: 'blur'},
          {min: 3, max: 15, message: '长度在3到15个字符', trigger: 'change'},
          {min: 3, max: 15, message: '长度在3到15个字符', trigger: 'blur'},
        ],
        checkPassword: [
          {required: true, message: '请输入确认密码', trigger: 'blur'},
          {min: 3, max: 15, message: '长度在3到15个字符', trigger: 'change'},
          {min: 3, max: 15, message: '长度在3到15个字符', trigger: 'blur'},
          {validator: validatePass2,trigger: 'blur'},
          {validator: validatePass2,trigger: 'change'},
        ]
      }
    }
  },
  methods: {
    // 确定修改
    submitForm(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 获取整个vue的this
          const _this = this;
          let fd = new FormData;
          fd.append('username',_this.$store.getters.getUser.username);
          fd.append('oldPassword',_this.ruleForm.oldPassword);
          fd.append('checkPassword',_this.ruleForm.checkPassword);
          _this.axios.post("/user/updatePassword", fd,{
            headers: {
              "Authorization": localStorage.getItem("token")
            }
          }).then((res) => {

            if (res.data.data === 'success'){
              _this.$message.success('密码修改成功!');
              //首先调用后端logout接口（因该接口需要认证权限，所以需要传入token）
              //其次调用$store清除用户信息及token
              _this.axios.get("/logout", {
                headers: {
                  "Authorization": localStorage.getItem("token")
                }
              }).then(res => {
                _this.$store.commit("REMOVE_INFO");
                _this.$router.push("/login");
              })
            }else {
              _this.$message.error('密码修改失败,旧密码错误,请重新输入!');
              this.$refs['ruleForm'].resetFields();
            }

          }).catch((error) => {
            console.log(error);
          });
        } else {
          console.log('error submit!');
          return false;
        }
      });
    },
    // 重置
    resetForm(formName){
      this.$refs[formName].resetFields();
    },
    // 关闭窗口
    updatePasswordClose(){
      this.$refs['ruleForm'].resetFields();
    }
  }
}
</script>

<style scoped>

</style>
