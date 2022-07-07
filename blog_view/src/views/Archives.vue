<template>
  <div class="archives-container">
    <el-timeline class="archives-timeline">
      <el-timeline-item v-for="blog in blogs" id="tiemline" :key="blog.id" color="deepskyblue" placement="top"
                        v-bind:timestamp="blog.created">
        <div class="blog-box-card">
          <router-link :to="{name: 'Blog',params: {blogId: blog.id}}" class="blog-link">{{ blog.title }}</router-link>
        </div>
      </el-timeline-item>
    </el-timeline>

    <div class="home-page" v-if="pageShow">
      <el-pagination
          :current-page="currentPage"
          :page-size="pageSize"
          :total="total"
          background
          layout="total,prev,pager,next"
          @current-change="getArchives"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: "Archives",
  data() {
    return {
      blogs: [],
      currentPage: 1,
      pageSize: 10,
      pageShow: 0,
      total: 0
    }
  },
  created() {
    this.getArchives(1);
  },
  methods: {
    getArchives(currentPage){
      this.axios.get('/blog/archives?currentPage=' + currentPage + '&pageSize=' + this.pageSize).then(res => {
        const blogData = res.data.data;
        this.blogs = blogData.records;
        this.currentPage = blogData.current;
        this.pageSize = blogData.size;
        this.total = blogData.total;
        this.pageShow = 1;

        this.scrollToTop();
      });
    }
  }
}
</script>

<style scoped>

.blog-box-card {
  background-color: white;
  padding-bottom: 0px;
  height: 50px;
}


.blog-link:link, .blog-link:visited {
  text-decoration: none;
  color: black;

}

.blog-link {
  height: 20px;
  font-weight: bold;
  line-height: 50px;
}

.blog-box-card:hover {
  -webkit-box-shadow: #ccc 0px 10px 10px;
  -moz-box-shadow: #ccc 0px 10px 10px;
  box-shadow: #ccc 0px 10px 10px;
}
.home-page {
  text-align: center;
}

/deep/.el-timeline-item__timestamp {
  color: #ffffff;
  line-height: 1;
  font-size: 13px;
  left: 0;
}
</style>
