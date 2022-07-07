<template>
  <div class="ui top segment" style="text-align: center">
    <h2 class="m-text-500">标签 {{ tagName }} 下的文章</h2>
    <el-card v-for="blog in blogs" :body-style="{padding: '0px'}" :key="blog.id" class="home-main-column-middle-card">
      <div class="home-title">
        <h2>
          <router-link :to="{name: 'Blog',params: {blogId: blog.id}}" class="blog-link">{{ blog.title }} </router-link>
        </h2>
        <span style="font-size: small;color: blue">创建时间：{{ blog.created }}</span>
        <span style="font-size: small;color: blue">更新时间：{{ blog.updateTime }}</span>
        <span style="font-size: small;color: blue">标签：{{ tagName }}</span>
      </div>
      <img :src="blog.firstPicture" class="image">

      <div class="home-description-markdown-body" v-html="blog.description"></div>
    </el-card>

    <div v-if="pageShow" class="home-page">
      <el-pagination
          :current-page="currentPage"
          :page-size="pageSize"
          :total="total"
          background
          layout="prev,pager,next,jumper"
          @current-change="getBlogByTagName"
      ></el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: "Tags",
  data() {
    return {
      tagName: '',
      blogs: [],
      tags: [],
      currentPage: 1,
      total: 0,
      pageSize: 5,
      pageShow: 0
    }
  },
  created() {
    this.tagName = this.$route.params.name;
    this.getBlogByTagName(1);
  },
  watch: {
    '$route'(){
      this.tagName = this.$route.params.name;
      // console.log(this.tagName);
      this.getBlogByTagName(1);
    }
  },
  methods: {
    getBlogByTagName(currentPage){
      this.axios.get('/blog/byBlogTagName?currentPage=' + currentPage + "&pageSize=" + this.pageSize +
          "&tagName=" + this.tagName).then(res => {
        const blogData = res.data.data;
        this.blogs = blogData.records;
        this.currentPage = blogData.current;
        this.pageSize = blogData.size;
        this.total = blogData.total;
        this.pageShow = 1;
      })
    }
  }
}
</script>

<style scoped>
.home-container {

}

.home-description-markdown-body {
  text-align: left;
}

.image {
  width: 100%;
  height: auto;
}

.home-main-column-middle-card {
  background-color: white;
  margin-bottom: 40px;

  padding: 20px 41px;
  text-align: center;


}

.home-title {
  margin-bottom: 20px;
}

.blog-link:link, .blog-link:visited {
  text-decoration: none;
  color: black;


}

.home-main-column-middle-card:hover {
  -webkit-box-shadow: #ccc 0px 10px 10px;
  -moz-box-shadow: #ccc 0px 10px 10px;
  box-shadow: #ccc 0px 10px 10px;
}

.home-page {
  text-align: center;
}
</style>
