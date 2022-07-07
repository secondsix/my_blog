<template>
  <div class="home-container">
    <el-card v-for="blog in blogs" :body-style="{padding: '0px'}" class="home-main-column-middle-card" :key="blog.id">
      <div class="home-title">
        <h2>
          <router-link :to="{name: 'Blog',params: {blogId: blog.id}}" class="blog-link">
            {{ blog.title }}
          </router-link>
        </h2>
        <span style="font-size: small;color: #0425da;margin-right: 5px">创建时间：{{ blog.created.split(' ')[0] }}</span>
<!--        <span style="font-size: small;color: #0425da">创建时间：{{ blog.created }}</span>-->
<!--        <span style="font-size: small;color: #0425da">更新时间：{{ blog.updateTime.split(' ')[0] }}</span>-->
        <span style="font-size: small;color: #0425da;margin-right: 5px">更新时间：{{ blog.updateTime }}</span>
        <span style="font-size: small;color: #0425da;margin-right: 5px">分类：{{ blog.typeName }}</span>
        <span style="font-size: small;color: #0425da;margin-right: 5px">标签：{{ blog.tagName }}</span>
        <span style="font-size: small;color: #0425da">字数：{{ blog.words }}</span>
      </div>

      <img :src="blog.firstPicture" class="image" v-viewer="{movable: false}">

      <markdown-it-vue-light class="home-description-markdown-body" :content="blog.description"
                             v-viewer="{movable: false}"/>

      <!-- 阅读全文按钮 -->
      <div class="div-btn">
        <a class="color-btn" href="javascript:;" @click.prevent="toBlog(blog)">阅读全文</a>
      </div>
    </el-card>

    <!--  分页  -->
    <div v-if="pageShow" class="home-page">
      <el-pagination
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        background
        layout="prev,pager,next,jumper"
        @current-change=getDataSource
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      blogs: [],
      types: [],
      currentPage: 1,
      total: 0,
      pageSize: 5,
      pageShow: 0
    }
  },
  created() {
    this.getDataSource(1);
  },
  methods: {
    toBlog(blog){
      //跳转到博客详情页
      this.$router.push(`/blog/${blog.id}`)
    },
    getDataSource(currentPage){
      // console.log(currentPage);
      if (currentPage !== 1){
        $('#waypoint').hide();
        $('#header-photo').hide();
      }else {
        $('#waypoint').show();
        $('#header-photo').show();
      }
      const _this = this;

      /*{
        headers: {
          "Authorization": localStorage.getItem("token")
        }
      }*/

      this.axios.get('/blog/getData?pageNo=' + currentPage + "&pageSize=" + this.pageSize + "&status=" + 1, ).then(res => {
        // console.log(res);
        _this.blogs = res.data.data.records;
        _this.currentPage = res.data.data.current;
        _this.pageSize = res.data.data.size;
        _this.total = res.data.data.total;
        _this.pageShow = 1;
      });

      //改变页号后返回顶部
      this.scrollToTop();

    }
  }
}
</script>

<style scoped>
.home-container {


}

/*.home-description-markdown-body {*/
/*  text-align: left;*/
/*}*/
home-description-markdown-body {
  text-align: left !important;
}

.image {
  width: 100%;
  height: auto;
}

.home-main-column-middle-card {
  background-color: white;
  margin-bottom: 40px;

  padding: 20px 41px;

}

.home-title {
  text-align: center;
}

.home-page {
  text-align: center;
}

.home-title {
  margin-bottom: 20px;
}

.div-btn {
  text-align: center;
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

.color-btn {

  text-decoration-line: none;
  padding: 11px 20px;
  color: white;
  font-size: 13px;
  background: linear-gradient(to right, #56ccf2, #2f80ed);
  border-radius: 15px;
  min-width: 90px;

}

.div-btn {
  padding: 14px 5px 30px 5px;
  margin-top: 7px;

}

/deep/ .el-pagination__jump{
 color: white;
}
</style>
