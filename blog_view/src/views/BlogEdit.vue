<template>
<!-- 博客编辑页 -->
  <div>
<!--    <h1>博客编辑页</h1>-->
    <BlogHeader></BlogHeader>
    <div class="m-content">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="标题" prop="title">
          <el-input v-model="ruleForm.title"></el-input>
        </el-form-item>
        <el-form-item label="摘要" prop="description">
          <el-input v-model="ruleForm.description"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <mavon-editor v-model="ruleForm.content"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
          <el-button  @click="resetForm('ruleForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import BlogHeader from "@/views/Header";

export default {
  name: "BlogEdit",
  components:{
    BlogHeader
  },
  data(){
    return{
      ruleForm:{
        id: '',
        title: '',
        description: '',
        content: ''
      },
      rules:{
        title:[
          {required: true,message:"请输入标题",trigger: 'blur'},
          {min: 3,max: 5,message: "长度在3到15个字符",trigger: 'blur'}
        ],
        description:[
          {required: true,message:"请输入摘要",trigger: 'blur'},
        ],
        content:[
          {required: true,message:"请输入内容",trigger: 'blur'},
        ]
      },
    }
  },
  methods:{
    submitForm(formName){
      this.$refs[formName].validate((valid)=>{
        if (valid){
          const _this = this;
          this.axios.post('/blog/edit',this.ruleForm,{
            headers:{
              "Authorization": localStorage.getItem("token")
            }
          }).then(res =>{
            console.log(res);
            _this.$alert("操作成功",'提示',{
              confirmButtonText: '确定',
              callback: action => {
                console.log(action);
                _this.$router.push("/blogs");
              }
            });
          });
        }else {
          console.log('error submit!');
          return false;
        }
      });
    },
    resetForm(formName){
      this.$refs[formName].resetFields;
    }

  },
  created() {
    //获取动态路由的blogid
    const blogId = this.$route.params.blogid;
    console.log(blogId);
    const _this = this;
    if (blogId){
      this.axios.get('/blog/'+blogId).then(res =>{
        console.log(res);
        const blog = res.data.data;
        _this.ruleForm.id = blog.id;
        _this.ruleForm.title = blog.title;
        _this.ruleForm.description = blog.description;
        _this.ruleForm.content = blog.content;

      })
    }

  }
}
</script>

<style scoped>
.m-content {
  text-align: center;
}
</style>
