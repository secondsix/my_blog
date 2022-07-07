<template>
  <div class="blog-detail">
    <div class="mblog">
<!--      <div class="home-title">
        <h2>{{ blog.title }}</h2>
      </div>-->

      <el-link v-if="ownBlog" icon="el-icon-edit">
        <router-link :to="{name: 'BlogEdit',params: {blogId: blog.id}}">
          编辑
        </router-link>
      </el-link>
<!--      <el-divider></el-divider>-->
      <div class="markdown-body" v-html="blog.content"></div>
    </div>

    <!--版权信息-->
    <div class="author-message">

      <ul class="list">
        <li>作者：Arthur
          <router-link style="text-decoration-line: none;" to="/about">（联系作者）</router-link>
        </li>
        <li>发表时间：{{ blog.createTime }}</li>
        <li>最后修改：{{ blog.updateTime }}</li>
        <li>本站点采用<a href="https://creativecommons.org/licenses/by/4.0/" style="text-decoration-line: none;"
                    target="_blank"> 署名 4.0 国际 (CC BY 4.0) </a>创作共享协议。可自由转载、引用，并且允许商业性使用。但需署名作者且注明文章出处。
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: "About",
  data() {
    return {
      blog: {},
      ownBlog: false
    }
  },
  created() {
    this.scrollToTop();
    this.getAbout();
  },
  methods: {
    getAbout(){
      this.axios.get('/blog/about').then(res => {
        this.blog = res.data.data;
        // console.log(this.blog);

        var MardownIt = require("markdown-it");
        var md = new MardownIt();

        this.blog.content = md.render(res.data.data.content);
        // console.log(this.$store.getters.getUser)
        if (this.$store.getters.getUser){
          this.ownBlog = (res.data.data.userId === this.$store.getters.getUser.id);
        }
      })
    }
  }
}
</script>

<style scoped>
.mblog {
  min-height: 600px;


  padding: 5px 35px 5px 35px;
  text-align: left;
}

.home-title {
  margin-bottom: 40px;
}

.markdown-body {
  text-align: left;
}

.blog-detail {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  background-color: white;
}

.author-message {
  text-align: left;
  background-color: honeydew;
  padding: 10px 5px 10px 5px;
  font-size: 14px;

}

.el-divider {
  margin: 1rem 0 !important;
}
</style>
