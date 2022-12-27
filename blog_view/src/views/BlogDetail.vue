<template>
<!-- 博客详情页 -->
  <div>
<!--    <h1>博客详情页</h1>-->
    <BlogHeader></BlogHeader>
    <div class="blog">
      <h2>{{blog.title}}</h2>
      <el-link icon="el-icon-edit" v-if="ownBlog" class="linklist">
        <router-link :to="{name:'BlogEdit',params:{blogid:blog.id}}">
          编辑
        </router-link>
      </el-link>

      <el-link icon="el-icon-delete" v-if="ownBlog" class="linklist">
        <el-button type="danger" round @click="delblog">删除</el-button>
      </el-link>

      <el-divider></el-divider>

      <div class="markdown-body" v-html="blog.content"></div>
    </div>
  </div>
</template>

<script>
import BlogHeader from "@/views/Header";
import 'github-markdown-css'

export default {
  name: "BlogDetail",
  components:{
    BlogHeader
  },
  data(){
    return{
      blog:{
        id: '',
        title: '',
        content: '',
        description: ''
      },
      ownBlog: false,
    }
  },
  methods:{
    delblog(){
      const blogId = this.$route.params.blogid;
      const _this = this;
      if (blogId){
        this.$confirm('此操作将永久删除该文章,是否继续？','提示',{
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(()=>{
          _this.axios.post('/blog/blogdel/'+blogId,null,{
            headers:{
              "Authorization": localStorage.getItem("token")
            }
          }).then(res => {
            this.$message({
              type: 'success',
              message: res.data.data
            });
            _this.$router.push('/blogs');
          });
        }).catch(()=>{
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
      }
    }
  },
  created() {
    //获取动态路由的blogid
    const blogId = this.$route.params.blogid;
    const _this = this;
    if (blogId){
      this.axios.get("/blog/"+blogId).then(res =>{
        const blog = res.data.data;
        _this.blog.id = blog.id;
        _this.blog.title = blog.title;
        _this.blog.description = blog.description;

        //mardownIt 渲染
        var MardownIt = require("markdown-it");
        var md = new MardownIt();
        var result = md.render(blog.content);
        _this.blog.content = result;
        //查看是否是登陆人 是则可以编辑和删除
        // _this.ownBlog = (blog.userId === _this.$store.getters.getUser.id);
      })
    }
  }
}
</script>

<style scoped>
.blog {
  margin-top: 10px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  width: 100%;
  min-height: 700px;
  padding: 10px;
}
</style>
